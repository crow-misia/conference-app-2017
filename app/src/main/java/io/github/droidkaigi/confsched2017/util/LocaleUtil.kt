package io.github.droidkaigi.confsched2017.util

import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.text.TextUtils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import io.github.droidkaigi.confsched2017.BuildConfig
import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import timber.log.Timber

object LocaleUtil {

    private val DEFAULT_LANG = Locale.ENGLISH
    @JvmStatic
    val SUPPORT_LANG = arrayListOf(Locale.JAPANESE, Locale.ENGLISH)

    private val TAG = LocaleUtil::class.java.simpleName

    private val CONFERENCE_TIMEZONE = TimeZone.getTimeZone(BuildConfig.CONFERENCE_TIMEZONE)

    @JvmStatic
    fun initLocale(context: Context) {
        setLocale(context, getCurrentLanguageId(context))
    }

    @JvmStatic
    fun setLocale(context: Context, languageId: String) {
        val config = context.resources.configuration
        DefaultPrefs.get(context).putLanguageId(languageId)
        val locale = Locale(languageId)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        // updateConfiguration, deprecated in API 25.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }

    @JvmStatic
    fun getCurrentLanguageId(context: Context): String {
        // This value would be stored language id or empty.
        var languageId = DefaultPrefs.get(context).languageId
        if (TextUtils.isEmpty(languageId)) {
            languageId = LocaleUtil.getLocaleLanguageId(Locale.getDefault())
        }

        return if (SUPPORT_LANG.any { TextUtils.equals(languageId, LocaleUtil.getLocaleLanguageId(it)) }) languageId else LocaleUtil.getLocaleLanguageId(DEFAULT_LANG)
    }

    @JvmStatic
    fun getLocaleLanguageId(locale: Locale): String {
        return locale.language.toLowerCase()
    }

    @JvmStatic
    fun getCurrentLanguage(context: Context): String {
        return context.getString(getLanguage(LocaleUtil.getCurrentLanguageId(context)))
    }

    @JvmStatic
    fun getDisplayLanguage(context: Context, locale: Locale): String {
        val languageId = getLocaleLanguageId(locale)
        return getDisplayLanguage(context, "lang_" + languageId + "_in_" + languageId)
    }

    private fun getDisplayLanguage(context: Context, resName: String): String {
        try {
            val resourceId = context.resources.getIdentifier(
                    resName, "string", context.packageName)
            if (resourceId > 0) {
                return context.getString(resourceId)
            } else {
                Timber.tag(TAG).d("String resource id: %s is not found.", resName)
                return ""
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "String resource id: %s is not found.", resName)
            return ""
        }

    }

    @StringRes
    fun getLanguage(languageId: String): Int {
        if (TextUtils.equals(languageId, getLocaleLanguageId(Locale.ENGLISH))) {
            return R.string.lang_en
        } else if (TextUtils.equals(languageId, getLocaleLanguageId(Locale.JAPANESE))) {
            return R.string.lang_ja
        } else {
            return R.string.lang_en
        }
    }

    fun getDisplayDate(date: Date, context: Context): Date {
        val formatTokyo = SimpleDateFormat.getDateTimeInstance()
        formatTokyo.timeZone = CONFERENCE_TIMEZONE
        val formatLocal = SimpleDateFormat.getDateTimeInstance()
        formatLocal.timeZone = getDisplayTimeZone(context)
        try {
            return formatLocal.parse(formatTokyo.format(date))
        } catch (e: ParseException) {
            Timber.tag(TAG).e(e, "date: %s can not parse.", date.toString())
            return date
        }

    }

    fun getDisplayTimeZone(context: Context): TimeZone {
        val defaultTimeZone = TimeZone.getDefault()
        val shouldShowLocalTime = DefaultPrefs.get(context).showLocalTimeFlag
        return if (shouldShowLocalTime && defaultTimeZone != null) defaultTimeZone else CONFERENCE_TIMEZONE
    }
}
