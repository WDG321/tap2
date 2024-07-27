package com.app.tap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.tap2.components.CloseFloatingWindowButton
import com.app.tap2.components.EnableFloatingWindowButton
import com.app.tap2.components.RequestAccessibilityAccessButton
import com.app.tap2.ui.theme.Tap2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用全屏显示,不然会显示在状态栏下面
        enableEdgeToEdge()
        setContent {
            Tap2Theme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) { innerPadding ->

                    // 使用约束布局
                    ConstraintLayout(
                        modifier = Modifier
                            // padding 在元素周围留出空间
                            .padding(innerPadding)
                            // 填充父组件所有可用空间
                            .fillMaxSize()
                    ) {

                        // 创建水平参考线，距离顶部 50% 的位置
                        val guidelineTop = createGuidelineFromTop(0.5f)

                        // 创建垂直参考线，距离左侧 50% 的位置
                        // val guidelineStart = createGuidelineFromStart(0.5f)
                        // 创建参照点
                        val (
                            requestAccessibilityAccessButton,
                            enableFloatingWindowButton,
                            closeFloatingWindowButton
                        ) = createRefs()
                        RequestAccessibilityAccessButton(
                            modifier = Modifier
                                .constrainAs(requestAccessibilityAccessButton) {
                                    // parent 是一个现有的引用，可用于指定对 ConstraintLayout 可组合项本身的约束条件
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(enableFloatingWindowButton.top)
                                },
                            snackBarHostState = snackbarHostState
                        )
                        EnableFloatingWindowButton(
                            modifier = Modifier
                                .constrainAs(enableFloatingWindowButton) {
                                    // parent 是一个现有的引用，可用于指定对 ConstraintLayout 可组合项本身的约束条件
                                    bottom.linkTo(guidelineTop)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            snackBarHostState = snackbarHostState
                        )
                        CloseFloatingWindowButton(
                            modifier = Modifier
                                .constrainAs(closeFloatingWindowButton) {
                                    // parent 是一个现有的引用，可用于指定对 ConstraintLayout 可组合项本身的约束条件
                                    top.linkTo(guidelineTop)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            snackBarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}*/

// 使用@Preview设置显示AndroidStudio预览的内容
/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Tap2Theme {
        StartOrEndButton(id=6)
    }
}*/
