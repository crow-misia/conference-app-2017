package io.github.droidkaigi.confsched2017.pref

import com.chibatching.kotpref.KotprefModel

object DefaultPrefs : KotprefModel() {
    override val kotprefName = "io.github.droidkaigi.confsched_preferences"

    var languageId by stringPref("", "current_language_id")

    var notificationFlag by booleanPref(true, "notification_setting")

    var headsUpFlag by booleanPref(true, "heads_up_setting")

    var showLocalTimeFlag by booleanPref(false, "show_local_time")

    var notificationTestFlag by booleanPref(false, "notification_test_setting")

    var showDebugOverlayView by booleanPref(false, "show_debug_overlay_view")
}
