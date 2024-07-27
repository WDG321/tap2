package com.app.tap2.utils

import android.accessibilityservice.AccessibilityService.WINDOW_SERVICE
import android.content.Context
import android.graphics.Rect
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics

// 将LayoutParams的xy转换成屏幕上的xy位置
fun convertLayoutParamsToWindowPosition(
    context: Context,
    layoutParamsX: Int,
    layoutParamsY: Int
): IntArray {
    val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    val windowMetrics: WindowMetrics =
        windowManager.currentWindowMetrics
    val bounds: Rect = windowMetrics.bounds

    // 窗口宽高,但不包括系统栏的宽高度
    val screenWidth: Int = bounds.width()
    val screenHeight: Int = bounds.height()
    // 获取状态栏信息
    // 获取窗口插图的代码。窗口插图（WindowInsets）表示系统窗口（如状态栏、导航栏和软件键盘）占用的空间。
    val insets = windowMetrics.windowInsets
        // 这个方法用于获取指定类型系统窗口（如状态栏和导航栏）的插图，并忽略它们的可见性状态（即，无论状态栏和导航栏是否可见，都返回它们的高度）。
        .getInsetsIgnoringVisibility(
            /*
            WindowInsets.Type 类包含了许多常量，这些常量表示不同类型的系统窗口区域。下面是 WindowInsets.Type 中一些常用属性的详细解释：
                systemBars()
                    包含系统栏，包括状态栏、导航栏和标题栏。
                    用于表示这些区域的插图信息。
                statusBars()
                    仅包含状态栏。
                    用于获取状态栏区域的插图信息。
                navigationBars()
                    仅包含导航栏。
                    用于获取导航栏区域的插图信息。
                captionBar()
                    包含标题栏。
                    用于获取标题栏区域的插图信息。
                ime()
                    包含输入法窗口（软键盘）。
                    于获取输入法窗口区域的插图信息。
                systemGestures()
                    包含系统手势区域。
                    用于获取系统手势（例如屏幕边缘滑动手势）占用的插图信息。
                mandatorySystemGestures()
                    包含强制性系统手势区域。
                    用于获取强制性系统手势占用的插图信息。
                tappableElement()
                    包含可点击元素区域。
                    用于获取可点击元素（如导航栏按钮）占用的插图信息。
                displayCutout()
                    包含屏幕切口（如刘海屏、水滴屏）。
                    用于获取屏幕切口区域的插图信息。*/
            WindowInsets.Type.statusBars()
        )
    // 由于layoutParams是以屏幕正中心为0,0点,所以转换需要把坐标系往左上角挪,然后计算就是加上屏幕宽高除以2
    val windowX = layoutParamsX + ((screenWidth) / 2)
    val windowY = layoutParamsY + ((screenHeight + insets.top) / 2)// 高度需要加上状态栏高度
    val windowPosition = IntArray(2)
    windowPosition[0] = windowX
    windowPosition[1] = windowY
    return windowPosition
}