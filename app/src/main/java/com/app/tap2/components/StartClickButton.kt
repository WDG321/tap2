package com.app.tap2.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.app.tap2.services.MyAccessibilityService

@Composable
// 开始点击按钮
fun StartClickButton(id: Int, buttonText: String) {
    // 获取当前组件的View实例
    val view = LocalView.current
    // val context = LocalContext.current

    Box(
        Modifier
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    MyAccessibilityService.updateFloatingWindowLayout(
                        dragAmount.x.toInt(),
                        dragAmount.y.toInt(),
                        id
                    )
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
            },
            colors = ButtonDefaults.buttonColors(
                // 内容颜色
                contentColor = Color.Black,
                // 容器颜色
                containerColor = Color.White.copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = buttonText,
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

