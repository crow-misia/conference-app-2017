package io.github.droidkaigi.confsched2017.util

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.text.format.DateUtils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object DateUtil {
    const val FORMAT_MMDD = "MMMd"

    const val FORMAT_KKMM = "kk:mm"

    const val FORMAT_YYYYMMDDKKMM = "yyyyMMMdkkmm"

    const val FORMAT_PROGRAM_START_DATE = "MM/dd(E) kk:mm"

    fun getMonthDate(date: Date, context: Context): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_MMDD)
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } else {
            val flag = DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_NO_YEAR
            return DateUtils.formatDateTime(context, date.time, flag)
        }
    }

    fun getHourMinute(date: Date): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_KKMM)
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } else {
            return DateFormat.format(FORMAT_KKMM, date).toString()
        }
    }

    fun getLongFormatDate(date: Date?): String {
        return date?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), FORMAT_YYYYMMDDKKMM)
                return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
            } else {
                val dayOfWeekFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG)
                val shortTimeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT)
                dayOfWeekFormat.timeZone = TimeZone.getDefault()
                shortTimeFormat.timeZone = TimeZone.getDefault()
                return dayOfWeekFormat.format(date) + " " + shortTimeFormat.format(date)
            }
        } ?: ""
    }

    fun getMinutes(stime: Date, etime: Date): Int {
        val range = etime.time - stime.time

        if (range > 0) {
            return (range / TimeUnit.MINUTES.toMillis(1L)).toInt()
        } else {
            return 0
        }
    }
}
