package io.github.droidkaigi.confsched2017.pref

import com.rejasupotaro.android.kvs.annotations.Key
import com.rejasupotaro.android.kvs.annotations.Table

@Table(name = "io.github.droidkaigi.confsched_preferences")
class DefaultPrefsSchema {
    @Key(name = "current_language_id")
    val languageId = ""

    @Key(name = "notification_setting")
    val notificationFlag = true

    @Key(name = "heads_up_setting")
    val headsUpFlag = true

    @Key(name = "show_local_time")
    val showLocalTimeFlag = false

    @Key(name = "notification_test_setting")
    val notificationTestFlag = false

    @Key(name = "show_debug_overlay_view")
    val showDebugOverlayView = false
}
