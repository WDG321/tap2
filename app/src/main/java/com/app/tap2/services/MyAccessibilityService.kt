package com.app.tap2.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.compositionContext
import com.app.tap2.MainActivity
import com.app.tap2.MyComposeViewLifecycleOwner
import com.app.tap2.roomSql.FloatingWindowEntity
import com.app.tap2.utils.getLayoutParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask


class MyAccessibilityService : AccessibilityService() {
    // 定时任务
    private var timer: Timer? = null

    // 接收息屏事件的广播接收器
    private var screenReceiver: BroadcastReceiver // 创建广播接收器实例
            = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 关闭点击
            closeClick()
        }
    }

    // 伴生类
    companion object {
        private var lifecycleOwners = mutableListOf<MyComposeViewLifecycleOwner>()
        private var windowManager: WindowManager? = null


        private val composeViews = mutableMapOf<Int, ComposeView>()
        private val layoutParamss = mutableMapOf<Int, WindowManager.LayoutParams>()

        // 悬浮窗开启状态
        var floatingWindowF: Boolean = false


        // 更新需要点击的屏幕位置
        // IntArray索引0为x,1为y
        fun updatePosition(xy: IntArray, id: Int) {
            MainActivity.floatingWindows[id]?.windowPositionX = xy[0]
            MainActivity.floatingWindows[id]?.windowPositionY = xy[1]
        }

        // 如果给单例类(object)和伴生类中(companion object)的方法加上@JvmStatic注解，就会成为真正的静态方法，在kotlin和java文件中都可以调用。
        // @JvmStatic
        // 创建悬浮窗函数
        fun createFloatingWindow(
            context: Context,
            composable: @Composable () -> Unit,
            floatingWindow: FloatingWindowEntity

        ) {
            val layoutParams = getLayoutParams(
                floatingWindow.layoutParamsPositionX,
                floatingWindow.layoutParamsPositionY,
                floatingWindow.width,
                floatingWindow.height
            )
            // 创建 ComposeView，并将 Composable 函数设置为其内容
            // ComposeView 是一个特殊的 Android View，用于在 Android 中承载 Compose 的 UI。
            // applicationContext是整个应用的上下文
            val composeView = ComposeView(context).apply {
                setContent {
                    composable()
                }
            }
            // 添加ViewLifecycleOwner
            // 注意，在 调用 addView 之前：
            lifecycleOwners.add(// 装进容器方便管理
                MyComposeViewLifecycleOwner().also {
                    it.onCreate() // 注意
                    it.attachToDecorView(composeView)
                }
            )
            // 执行重组的逻辑,不然在里面更新数据后无法更新视图
            val coroutineContext = AndroidUiDispatcher.CurrentThread
            val runRecomposeScope = CoroutineScope(coroutineContext)
            val recomposer = Recomposer(coroutineContext)
            composeView.compositionContext = recomposer
            runRecomposeScope.launch {
                recomposer.runRecomposeAndApplyChanges()
            }
            // 将悬浮窗控件添加到WindowManager
            windowManager?.addView(composeView, layoutParams)
            // 添加进map集合方便管理
            composeViews[floatingWindow.id] = composeView
            layoutParamss[floatingWindow.id] = layoutParams
            floatingWindowF = true
        }

        // @JvmStatic
        // 关闭悬浮窗函数
        fun closeFloatingWindow() {
            CoroutineScope(Dispatchers.Main).launch {
                // 将悬浮窗信息更新到数据库
                /* for ((_, floatingWindow) in MainActivity.floatingWindows) {
                     // 更新数据
                     MainActivity.dao?.updateFloatingWindow(floatingWindow)
                 }*/
                // 将悬浮窗信息更新到数据库
                MainActivity.dao?.updateFloatingWindows(MainActivity.floatingWindows.values.toMutableList())
                // 更新数据库完成后，清理相关数据
                for ((_, composeView) in composeViews) {
                    // 移除悬浮窗
                    windowManager?.removeViewImmediate(composeView)
                }
                // 清空集合
                composeViews.clear()
                layoutParamss.clear()
                for (lifecycleOwner in lifecycleOwners) {
                    lifecycleOwner.onDestroy()
                }
                lifecycleOwners.clear()
                floatingWindowF = false
            }
        }


        // 更新悬浮窗布局函数
        fun updateFloatingWindowLayout(x: Int, y: Int, id: Int) {
            val composeView = composeViews[id]
            val layoutParams = layoutParamss[id]
            // 变更位置
            layoutParams?.x = layoutParams?.x?.plus(x)
            layoutParams?.y = layoutParams?.y?.plus(y)

            // 更新悬浮窗的位置
            windowManager?.updateViewLayout(composeView, layoutParams)
            // 更新悬浮窗数据
            MainActivity.floatingWindows[id]?.layoutParamsPositionX = layoutParams?.x!!
            MainActivity.floatingWindows[id]?.layoutParamsPositionY = layoutParams.y
        }

        // 禁止所有悬浮窗响应点击
        private fun disableClickForAllFloatingWindows() {
            for ((id, layoutParams) in layoutParamss) {
                // 设置为不响应点击
                layoutParams.flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                windowManager?.updateViewLayout(composeViews[id], layoutParams)
            }
        }

        // 取消禁止所有悬浮窗响应点击
        private fun enableClickForAllFloatingWindows() {
            for ((id, layoutParams) in layoutParamss) {
                // 设置为响应点击
                layoutParams.flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                windowManager?.updateViewLayout(composeViews[id], layoutParamss[id])
            }
        }
    }

    // 当无障碍服务连接之后回调
    override fun onServiceConnected() {
        super.onServiceConnected()
        // 获取WindowManager服务,WindowManager是Android系统的一个关键类，用于管理窗口的显示、布局和交互。
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        // 创建意图过滤器，只接收ACTION_SCREEN_OFF(息屏)广播
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        // 注册广播接收器
        registerReceiver(screenReceiver, filter)
    }


    // 该方法会在无障碍服务接收到无障碍事件时被调用
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 自己设置无障碍事件类型为666就代表开始点击
        if (event?.eventType == 666) {
            timer = Timer()
            disableClickForAllFloatingWindows()
            val timerTask = object : TimerTask() {
                override fun run() {
                    // 在这里编写需要定时执行的函数逻辑
                    // 开始点击
                    performClick()
                }
            }
            // 每隔一段时间（例如1000毫秒）执行一次任务
            timer?.schedule(timerTask, 0, 100)
        }
    }

    /* 当系统想要中断您的服务正在提供的反馈（通常是为了响应将焦点移到其他
     控件等用户操作）时，就会调用此方法。此方法可能会在您的服务的整个生命
     周期内被调用多次。*/
    override fun onInterrupt() {
        Log.d("无障碍服务", "无障碍服务被系统中断了")
    }

    // 关闭服务时调用
    override fun onUnbind(intent: Intent?): Boolean {
        // 关闭悬浮窗
        closeFloatingWindow()
        // 清理数据
        windowManager = null
        // 取消注册广播接收器
        unregisterReceiver(screenReceiver)
        return super.onUnbind(intent)
    }

    // 进行点击
    private fun performClick() {
        // 创建一个 GestureDescription.Builder 对象，用于构建手势描述
        val gestureBuilder = GestureDescription.Builder()
        for ((id, floatingWindow) in MainActivity.floatingWindows) {
            // id为1代表开始点击的按钮,此位置不需要进行自动点击相关的操作
            if (id != 1) {
                // 创建一个 Path 对象，并将其起始点设置为 (x, y)
                val path = Path()
                // moveTo() 方法用于将路径的当前位置移动到指定的坐标 (x, y)，不绘制任何线条。
                path.moveTo(
                    floatingWindow.windowPositionX.toFloat(),
                    floatingWindow.windowPositionY.toFloat()
                )
                // lineTo()用于绘制路径,从路径的当前位置绘制一条直线到指定的坐标 (x, y)。
                // path.lineTo(500F, 850F)
                // 在手势描述中添加一个笔画 (StrokeDescription)，该笔画从 path 中的 (x, y) 开始，持续时间为 10 毫秒
                /* 各参数含义
                path: Path 对象，表示手势的轨迹。Path 是 Android 绘图类之一，它定义了一条连续的线段或曲线路径。你可以使用 Path 类提供的方法创建和编辑手势路径的形状。
                startTime: long 类型的参数，手势开始的时间延迟, 毫秒
                duration: long 类型的参数，手势持续的时间, 毫秒
                */
                gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 10))
            }

        }
        // 调用 dispatchGesture 方法，将手势发送出去
        /* 各参数含义
        gesture：要发送的手势对象，通常是通过 GestureBuilder 构建的手势对象。
        callback：手势执行结果的回调接口。当手势执行完成时，系统会调用此回调接口来通知应用程序手势执行的结果。如果不需要处理手势执行结果，可以传入 null。
        handler：用于指定回调接口在哪个线程执行的 Handler 对象。如果传入 null，则回调接口会在主线程执行。*/
        dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription?) {
                // 点击操作被取消时的处理逻辑
                // 关闭点击
                closeClick()
            }

            override fun onCompleted(gestureDescription: GestureDescription?) {
                // 点击操作完成时的处理逻辑
                // Log.d("Gesture", "Click gesture completed")
            }
        }, null)
    }

    // 关闭点击
    private fun closeClick() {
        timer?.cancel()
        enableClickForAllFloatingWindows()
        // 发送广播,通知开启悬浮窗按钮:悬浮窗已关闭
        val intent = Intent("点击取消")
        sendBroadcast(intent)
    }

}
