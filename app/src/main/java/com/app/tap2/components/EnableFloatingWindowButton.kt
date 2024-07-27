package com.app.tap2.components

import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.app.tap2.services.MyAccessibilityService
import com.app.tap2.utils.isAccessibilityServiceEnabled
import com.app.tap2.utils.showSnackbar

// 开启悬浮窗的按钮
@Composable
fun EnableFloatingWindowButton(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
) {
    // LocalContext.current是一个提供当前上下文（Context）的Composition本地属性（CompositionLocal）。它用于获取当前的Context对象，以便在Composable函数中使用。
    val context = LocalContext.current
    // rememberCoroutineScope() 确保协程作用域的生命周期与组件的生命周期相关联，使得在组件	被重新构建时，协程作用域不会被重复创建。
    val scope = rememberCoroutineScope()
    // 获取数据库实例
    // by lazy关键字表示延时初始化变量，只在第一次使用时才给它初始化
    // 使用 lazy 委托，让实例 database 的创建延迟到首次需要/访问引用时（而不是在应用启动时创建）。这样将在首次访问时创建数据库（磁盘上的物理数据库）。
    /*val database: FloatingWindowRoomDatabase by lazy {
        FloatingWindowRoomDatabase.getDatabase(
            context
        )
    }*/
    /* val dao = remember { database.floatingWindowDao() }*/
    // DisposableEffect 可以感知 Composable 的 onActive 和 onDispose, 允许使用该函数完成一些预处理和收尾工作。
    // 典型的使用的场景，注册与取消注册
    // 这里首先参数 keys 表示，当 keys 变化时， DisposableEffect 会重新执行，如果在整个生命周期内，只想执行一次，则可以传入 Unit
    /*DisposableEffect(Unit) {

        onDispose {
            // 关闭数据库
            database.close()
        }
    }*/
    Button(
        modifier = modifier,
        onClick = {
            // 检查是否获取无障碍权限
            val isAccessibilityEnabled = isAccessibilityServiceEnabled(
                context,
                "com.app.tap2/com.app.tap2.services.MyAccessibilityService"
            )
            // 悬浮窗已经开启了不能再次进行操作
            if (MyAccessibilityService.floatingWindowF) {
                showSnackbar(snackBarHostState, scope, "悬浮窗已开启")
            } else {
                // 当无障碍权限获取后才能开启悬浮窗
                if (isAccessibilityEnabled) {
                    // 根据数据库内容创建目标点击位置悬浮窗
                    for ((id, floatingWindow) in MyAccessibilityService.floatingWindows) {
                        // id为1代表开始点击的按钮,需要特殊处理,而不是统一创建TargetPointIndicator,而是改为StartClickButton
                        if (id != 1) {
                            // 创建目标点击位置的悬浮窗
                            MyAccessibilityService.createFloatingWindow(
                                context,
                                { TargetPointIndicator(id = id) },
                                floatingWindow
                            )
                        } else {
                            // 创建开始点击的悬浮窗
                            MyAccessibilityService.createFloatingWindow(
                                context,
                                { StartClickButton(id = id, buttonText = "开始点击") },
                                floatingWindow
                            )
                        }
                    }
                } else {
                    showSnackbar(snackBarHostState, scope, "未获取无障碍权限")
                }
            }
        }) {
        Text(text = "开启悬浮窗")
    }
}