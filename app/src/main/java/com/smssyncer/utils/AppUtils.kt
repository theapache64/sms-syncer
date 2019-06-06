package com.smssyncer.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager


object AppUtils {

    fun hideApp(context: Context, splashActivity: Class<*>) {
        val packageManager = context.packageManager
        val componentName = ComponentName(context, splashActivity)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}