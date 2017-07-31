package io.github.droidkaigi.confsched2017.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.provider.Settings

/**
 * @author KeithYokoma
 */
object SettingsUtil {
    @JvmStatic
    fun canDrawOverlays(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return Settings.canDrawOverlays(context)
        return PermissionUtil.isPermissionGranted(context, Manifest.permission.SYSTEM_ALERT_WINDOW)
    }
}
