package io.github.droidkaigi.confsched2017.pref

import android.content.Context

class DefaultPrefs private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("io.github.droidkaigi.confsched_preferences", Context.MODE_PRIVATE)

    var languageId = ""
        get() = prefs.getString("current_language_id", field)
        set(value) = prefs.edit().putString("current_language_id", value).apply()

    var notificationFlag = true
        get() = prefs.getBoolean("notification_setting", field)
        set(value) = prefs.edit().putBoolean("notification_setting", value).apply()

    var headsUpFlag = true
        get() = prefs.getBoolean("heads_up_setting", field)
        set(value) = prefs.edit().putBoolean("heads_up_setting", value).apply()

    var showLocalTimeFlag = false
        get() = prefs.getBoolean("show_local_time", field)
        set(value) = prefs.edit().putBoolean("show_local_time", value).apply()

    var notificationTestFlag = false
        get() = prefs.getBoolean("notification_test_setting", field)
        set(value) = prefs.edit().putBoolean("notification_test_setting", value).apply()

    var showDebugOverlayView = false
        get() = prefs.getBoolean("show_debug_overlay_view", field)
        set(value) = prefs.edit().putBoolean("show_debug_overlay_view", value).apply()

    companion object {
        fun get(context: Context) = DefaultPrefs(context)
    }
}
