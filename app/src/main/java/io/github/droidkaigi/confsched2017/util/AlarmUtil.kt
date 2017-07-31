package io.github.droidkaigi.confsched2017.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build

import java.util.concurrent.TimeUnit

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import io.github.droidkaigi.confsched2017.receiver.NotificationReceiver

object AlarmUtil {

    private val REMIND_DURATION_MINUTES_FOR_START = TimeUnit.MINUTES.toMillis(10)

    @JvmStatic
    fun registerAlarm(context: Context, session: Session) {
        var time = session.stime.time - REMIND_DURATION_MINUTES_FOR_START

        val prefs = DefaultPrefs.get(context)
        if (prefs.notificationTestFlag) {
            time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5)
        }

        if (System.currentTimeMillis() < time) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(context, session))
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(context, session))
            }
        }
    }

    @JvmStatic
    fun unregisterAlarm(context: Context, session: Session) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(createAlarmIntent(context, session))
    }

    private fun createAlarmIntent(context: Context, session: Session): PendingIntent {
        val title = context.getString(R.string.notification_title, session.title)
        val displaySTime = LocaleUtil.getDisplayDate(session.stime, context)
        val displayETime = LocaleUtil.getDisplayDate(session.etime, context)
        val room = session.room?.name ?: ""
        val text = context.getString(R.string.notification_message,
                DateUtil.getHourMinute(displaySTime),
                DateUtil.getHourMinute(displayETime),
                room)
        val intent = NotificationReceiver.createIntent(context, session.id, title, text)
        return PendingIntent.getBroadcast(context, session.id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
