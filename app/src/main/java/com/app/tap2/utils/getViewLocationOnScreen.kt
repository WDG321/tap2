package com.app.tap2.utils

import android.view.View

// 获取view的中心点在屏幕上的位置坐标(而非默认的左上角坐标)
fun getViewLocationOnScreen(view: View): IntArray {
    val locationOnScreen = IntArray(2)
    // getLocationOnScreen：返回视图在屏幕上的绝对位置，包括所有系统窗口。
    // getLocationInWindow：返回视图在其窗口中的相对位置，不包括系统窗口的影响。
    view.getLocationOnScreen(locationOnScreen)
    // 获取的是左上角的位置
    // 计算正中间位置
    val center = IntArray(2)
    center[0] = locationOnScreen[0] + view.width / 2
    center[1] = locationOnScreen[1] + view.height / 2
    return center
}