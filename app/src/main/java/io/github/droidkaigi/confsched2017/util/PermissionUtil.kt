package io.github.droidkaigi.confsched2017.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * @author KeithYokoma
 */
object PermissionUtil {
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        try {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (t: Throwable) {
            return false
        }

    }
}
