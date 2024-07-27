package com.app.tap2.components

import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.app.tap2.utils.isAccessibilityServiceEnabled
import com.app.tap2.utils.showSnackbar

@Composable
// 获取无障碍权限按钮
fun RequestAccessibilityAccessButton(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState
) {
    // LocalContext.current是一个提供当前上下文（Context）的Composition本地属性（CompositionLocal）。它用于获取当前的Context对象，以便在Composable函数中使用。
    val context = LocalContext.current
    // rememberCoroutineScope() 确保协程作用域的生命周期与组件的生命周期相关联，使得在组件	被重新构建时，协程作用域不会被重复创建。
    val scope = rememberCoroutineScope()
    Button(
        modifier = modifier,
        onClick = {
            // 检查是否获取无障碍权限
            val isAccessibilityEnabled = isAccessibilityServiceEnabled(
                context,
                "com.app.tap2/com.app.tap2.services.MyAccessibilityService"
            )
            if (!isAccessibilityEnabled) {
                // 前往获取权限页面
                // 定义一个意图,意图可以用于跳转页面,发送广播,绑定服务等
                val intent = Intent(
                    // 这是一个系统常量，表示要启动的活动是用于管理应用无障碍权限的设置页面。
                    Settings.ACTION_ACCESSIBILITY_SETTINGS
                )
                // 使用startActivity方法启动上面定义的Intent，即打开悬浮窗权限设置页面。
                startActivity(context, intent, null)
            } else {
                showSnackbar(snackBarHostState,scope,"已获取无障碍权限")
            }
        }) {
        Text(text = "去获取无障碍权限")
    }
}
