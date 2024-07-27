package com.app.tap2.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 显示消息提示
fun showSnackbar(snackBarHostState: SnackbarHostState, scope: CoroutineScope, message: String) {
    // currentSnackbarData 是 SnackbarHostState 的一个属性，它包含了当前正在显示的 Snackbar 的信息。
    // 如果没有 Snackbar 显示，这个属性将为 null。
    snackBarHostState.currentSnackbarData?.dismiss()// 关闭上一个显示的消息
    // 启动协程
    scope.launch {
        // 显示snackBar
        snackBarHostState.showSnackbar(
            // 提示信息
            message = message,
            // 持续时长
            duration = SnackbarDuration.Short,
            // 点击关闭区域的提示文本
            actionLabel = "关闭",
        )
    }
}