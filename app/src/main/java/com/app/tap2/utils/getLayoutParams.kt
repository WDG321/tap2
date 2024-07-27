package com.app.tap2.utils

import android.graphics.PixelFormat
import android.view.WindowManager

fun getLayoutParams(x: Int, y: Int, w: Int, h: Int): WindowManager.LayoutParams {
    // 设置悬浮窗类型
    //WindowManager.LayoutParams 是 Android 中用于描述窗口的布局参数的类。当你需要创建一个新的窗口，并指定它的属性时，通常会使用 WindowManager.LayoutParams 对象来设置这些参数。
    val layoutParams = WindowManager.LayoutParams(
        /*// 窗口宽度,WindowManager.LayoutParams.WRAP_CONTENT表示视图的尺寸将根据其内容进行调整
        WindowManager.LayoutParams.WRAP_CONTENT,
        // 窗口高度,WindowManager.LayoutParams.WRAP_CONTENT表示视图的尺寸将根据其内容进行调整
        WindowManager.LayoutParams.WRAP_CONTENT,*/
        w, h,
        // 窗口类型:
        // TYPE_ACCESSIBILITY_OVERLAY：这个Window类型是为无障碍服务设计的，用于创建无障碍悬浮窗。它允许悬浮窗显示在其他应用程序的上层，包括系统级别的UI元素。这使得无障碍服务可以监控和处理用户界面上的事件，并进行相应的操作。
        // TYPE_APPLICATION_OVERLAY：这个Window类型用于创建普通的应用程序悬浮窗。与TYPE_ACCESSIBILITY_OVERLAY不同，它只能显示在当前应用程序的上层，无法覆盖其他应用程序或系统级别的UI元素。这使得应用程序可以在自己的界面上创建悬浮窗，提供额外的功能或信息。
        WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
        // 窗口标识:
        // FLAG_NOT_FOCUSABLE 表示该窗口不会获得焦点，也不会接收键盘输入事件。
        // FLAG_NOT_TOUCH_MODAL 表示即使在窗口之外点击或触摸，该窗口也不会自动获取焦点。这意味着点击窗口之外的区域时，事件将传递给后面的窗口处理。
        // FLAG_NOT_TOUCHABLE  表示当前窗口将不再响应触摸事件，包括点击事件。这意味着点击或触摸该悬浮窗窗口时，事件将传递给后面的窗口处理。
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        // 像素格式
        // 使用32位的RGBA格式，支持全透明和半透明
        PixelFormat.RGBA_8888
    )
    //设置位置
    layoutParams.x = x
    layoutParams.y = y
    return layoutParams
}