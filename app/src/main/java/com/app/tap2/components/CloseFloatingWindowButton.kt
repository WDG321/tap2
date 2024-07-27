package com.app.tap2.components

import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.app.tap2.services.MyAccessibilityService
import com.app.tap2.utils.showSnackbar

// 关闭悬浮窗按钮
@Composable
fun CloseFloatingWindowButton(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState
) {
    // LocalContext.current是一个提供当前上下文（Context）的Composition本地属性（CompositionLocal）。它用于获取当前的Context对象，以便在Composable函数中使用。
    // val context = LocalContext.current
    // rememberCoroutineScope() 确保协程作用域的生命周期与组件的生命周期相关联，使得在组件	被重新构建时，协程作用域不会被重复创建。
    val scope = rememberCoroutineScope()
    // val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
            if (MyAccessibilityService.floatingWindowF) {
                MyAccessibilityService.closeFloatingWindow()
            } else {
                showSnackbar(snackBarHostState, scope, "未开启悬浮窗")
            }

        },
    ) {
        Text(text = "关闭悬浮窗")
    }
}