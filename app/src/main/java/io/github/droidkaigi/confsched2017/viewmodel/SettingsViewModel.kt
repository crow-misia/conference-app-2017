package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.os.Build
import android.view.View
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs

import io.github.droidkaigi.confsched2017.util.LocaleUtil
import javax.inject.Inject

class SettingsViewModel @Inject internal constructor() : BaseObservable(), ViewModel {

    var callback: Callback? = null

    fun shouldNotify(): Boolean = DefaultPrefs.notificationFlag

    val isHeadsUp: Boolean
        get() = DefaultPrefs.headsUpFlag

    fun shouldShowLocalTime(): Boolean = DefaultPrefs.showLocalTimeFlag

    fun useDebugOverlayView(): Boolean = DefaultPrefs.showDebugOverlayView

    fun onClickLanguage(view: View) {
        callback?.showLanguagesDialog()
    }

    fun getCurrentLanguage(context: Context) = LocaleUtil.getCurrentLanguage(context)

    val showHeadsUpSettingVisibility: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { View.VISIBLE } else { View.GONE }

    fun onCheckedHeadsUpSetting(isChecked: Boolean) {
        DefaultPrefs.headsUpFlag = isChecked
    }

    fun onCheckedShowLocalTimeSetting(isChecked: Boolean) {
        DefaultPrefs.showLocalTimeFlag = isChecked
    }

    fun onCheckedNotificationSetting(isChecked: Boolean) {
        DefaultPrefs.notificationFlag = isChecked
        callback?.changeHeadsUpEnabled(isChecked)
    }

    fun onCheckedDebugOverlayView(isChecked: Boolean) {
        DefaultPrefs.showDebugOverlayView = isChecked
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
