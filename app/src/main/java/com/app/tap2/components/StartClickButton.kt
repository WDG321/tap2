package com.app.tap2.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.app.tap2.services.MyAccessibilityService

@Composable
// 开始点击按钮
fun StartClickButton(id: Int) {
    val text = remember { mutableStateOf("开始点击") }
    // 获取当前组件的View实例
    val view = LocalView.current
    val context = LocalContext.current
    DisposableEffect(Unit) {
        // 创建广播接收器
        val dynamicReceiver = object : BroadcastReceiver() {
            // 接收到广播
            override fun onReceive(context: Context?, intent: Intent?) {
                text.value = "开始点击"
            }
        }
        // 注册广播接收器
        val filter = IntentFilter("点击取消")
        // RECEIVER_NOT_EXPORTED表示不接收其他应用的广播,RECEIVER_EXPORTED表示接收其他应用的广播
        // 在无障碍服务中发送的广播需要设置为 RECEIVER_EXPORTED才能接收到
        context.registerReceiver(dynamicReceiver, filter, RECEIVER_EXPORTED)
        onDispose {
            // 取消注册广播接收器
            context.unregisterReceiver(dynamicReceiver)
        }
    }
    Box(
        Modifier
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    MyAccessibilityService.updateFloatingWindowLayout(
                        dragAmount.x.toInt(),
                        dragAmount.y.toInt(),
                        id
                    )
                //    Log.d("66666", dragAmount.x.toString() + "," + dragAmount.y.toString())
                }
            }
        /*.background(Color.Red)*/,
        // 使子组件居中
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                // 发送需要点击的无障碍事件,类型设置为666代表需要点击
                view.sendAccessibilityEvent(666)
                text.value = "点击中..."
            },
            colors = ButtonDefaults.buttonColors(
                // 内容颜色
                contentColor = Color.Black,
                // 容器颜色
                containerColor = Color.White.copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = text.value,
                style = TextStyle(
                    // 设置为粗体
                    fontWeight = FontWeight.Bold,
                    // 设置阴影
                    shadow = Shadow(
                        // 阴影颜色
                        color = Color.White,
                        // 阴影偏移
                        offset = Offset(0f, 0f),
                        // 阴影模糊
                        blurRadius = 5f
                    )
                )
            )
        }
    }

}

