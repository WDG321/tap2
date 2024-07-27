package com.app.tap2.utils

import android.content.Context
import android.provider.Settings
import android.text.TextUtils

// 检查无障碍权限是否开启的函数
fun isAccessibilityServiceEnabled(context: Context, service: String): Boolean {
    var isAccessibilityEnabled = false
    // 获取已启用的无障碍服务列表
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    // 如果已启用的无障碍服务列表不为空
    if (!enabledServices.isNullOrEmpty()) {
        // 使用 TextUtils.SimpleStringSplitter 或者 String.split 方法来解析服务列表
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        // 遍历已启用的服务列表，检查特定服务是否存在
        while (colonSplitter.hasNext()) {
            val enabledService = colonSplitter.next()
            // 对比服务名称是否相同
            if (enabledService.equals(service, ignoreCase = true)) {
                isAccessibilityEnabled = true
                break
            }
        }
    }
    return isAccessibilityEnabled
}