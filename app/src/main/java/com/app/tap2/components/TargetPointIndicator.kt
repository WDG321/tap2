package com.app.tap2.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import com.app.tap2.services.MyAccessibilityService
import com.app.tap2.utils.getViewLocationOnScreen


@Composable
// 目标点击位置
fun TargetPointIndicator(modifier: Modifier = Modifier, id: Int) {
    // LocalContext.current是一个提供当前上下文（Context）的Composition本地属性（CompositionLocal）。它用于获取当前的Context对象，以便在Composable函数中使用。
    // val context = LocalContext.current
    // 获取当前组件的View实例
    val view = LocalView.current
    // DisposableEffect 可以感知 Composable 的 onActive 和 onDispose, 允许使用该函数完成一些预处理和收尾工作。
    // 典型的使用的场景，注册与取消注册
    // 这里首先参数 keys 表示，当 keys 变化时， DisposableEffect 会重新执行，如果在整个生命周期内，只想执行一次，则可以传入 Unit
    /*DisposableEffect(Unit) {

        onDispose {
        }
    }*/
    /* var offset by remember { mutableStateOf(Offset.Zero) }*/
    Icon(
        Icons.Rounded.Add,
        // 这个属性用于帮助示例障碍人群理解图标的作用,户点击这个控件。android系统会自动使用人声朗读属性的内容
        contentDescription = "点击目标",
        // 当每个LayoutModifierNodeCoordinator的全局内容发生变化的时候，例如位置，大小等，onGloballyPositioned函数会被执行，这个过程会在组合完成后。
        modifier
            /*.onGloballyPositioned { coordinates ->
                // 获取相对于窗口的位置数据
                Log.d(
                    "66666666",
                    coordinates
                        .positionInParent()
                        .toString()
                )
            }*/
            // 设置组件的位置
            /*.offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }*/
            // pointerInput对button组件不生效
            // pointerInput无法同时处理拖动和点击事件,只能处理其中一个
            .pointerInput(Unit) {
                // 拖动相关处理
                /*detectDragGestures	监听拖动手势
                detectDragGesturesAfterLongPress	监听长按后的拖动手势
                detectHorizontalDragGestures	监听水平拖动手势
                detectVerticalDragGestures	监听垂直拖动手势*/
                detectDragGestures(
                    // 拖动开始
                    onDragStart = { _ ->
                        // Log.d("拖动开始", "拖动开始")
                    },
                    // 拖动结束
                    onDragEnd = {
                        // 获取位置
                        val location = getViewLocationOnScreen(view)
                        // 更新需要点击的位置
                        MyAccessibilityService.updatePosition(location, id)
                        Log.d("拖动结束", location[0].toString() + "," + location[1].toString())

                    },
                    // 拖动被取消
                    onDragCancel = {
                        // Log.d("拖动取消", "拖动取消")
                    }
                )
                // 拖动中事件
                // change参数是表示拖动手势的状态变化的对象，而dragAmount参数是拖动的偏移量。
                { _, dragAmount ->
                    // change.positionChange()是记录拖动事件的瞬时位置变化,如果低于某个值就可以判断为误触,就不进行操作
                    // change.positionChangeIgnoreConsumed()同change.positionChange()但会忽略太小的瞬时位置变化的值
                    MyAccessibilityService.updateFloatingWindowLayout(
                        dragAmount.x.toInt(),
                        dragAmount.y.toInt(),
                        id
                    )
                }
                /*// 点击相关处理
            detectTapGestures(
                // 触摸事件
                // offset是从触点到Composable元素左上角的偏移，类型为Offset
                onPress = { offset ->
                    val locationOnScreen = IntArray(2)
                    // 获取视图在屏幕上的位置
                    view.getLocationOnScreen(locationOnScreen)
                    // 根据offset和视图在屏幕上的位置计算点击位置在屏幕中的位置
                    val screenX = locationOnScreen[0] + offset.x
                    val screenY = locationOnScreen[1] + offset.y
                    Log.d("Screen Coordinates", "X: $screenX, Y: $screenY")
                },
                // 点击事件
                onTap = {
                    Log.d("666", "onClick ")
                },
                // 双击事件
                onDoubleTap = {
                    Log.d("666", "onDoubleClick ")
                },
                // 长按事件
                onLongPress = {
                    Log.d("666", "onLongClick ")
                })*/
            }
            .background(Color.White.copy(alpha = 0.8F))
    )
}