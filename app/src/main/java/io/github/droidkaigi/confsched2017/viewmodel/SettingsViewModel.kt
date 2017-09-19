package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.os.Build
import android.view.View

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import io.github.droidkaigi.confsched2017.util.LocaleUtil

class SettingsViewModel @Inject internal constructor(private val defaultPrefs: DefaultPrefs) : BaseObservable(), ViewModel {

    var callback: Callback? = null

    fun shouldNotify(): Boolean = defaultPrefs.notificationFlag

    val isHeadsUp: Boolean
        get() = defaultPrefs.headsUpFlag

    fun shouldShowLocalTime(): Boolean = defaultPrefs.showLocalTimeFlag

    fun useDebugOverlayView(): Boolean = defaultPrefs.showDebugOverlayView

    fun onClickLanguage(view: View) {
        callback?.showLanguagesDialog()
    }

    fun getCurrentLanguage(context: Context) = LocaleUtil.getCurrentLanguage(context)

    val showHeadsUpSettingVisibility: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { View.VISIBLE } else { View.GONE }

    fun onCheckedHeadsUpSetting(isChecked: Boolean) {
        defaultPrefs.headsUpFlag = isChecked
    }

    fun onCheckedShowLocalTimeSetting(isChecked: Boolean) {
        defaultPrefs.showLocalTimeFlag = isChecked
    }

    fun onCheckedNotificationSetting(isChecked: Boolean) {
        defaultPrefs.notificationFlag = isChecked
        callback?.changeHeadsUpEnabled(isChecked)
    }

    fun onCheckedDebugOverlayView(isChecked: Boolean) {
        defaultPrefs.showDebugOverlayView = isChecked
        callback?.debugOverlayViewEnabled(isChecked)
    }

    override fun destroy() {
        this.callback = null
    }

    interface Callback {
        fun changeHeadsUpEnabled(enabled: Boolean)

        fun showLanguagesDialog()

        fun debugOverlayViewEnabled(enabled: Boolean)
    }
}
