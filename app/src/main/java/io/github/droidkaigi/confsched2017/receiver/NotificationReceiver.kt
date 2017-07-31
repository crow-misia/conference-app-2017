package io.github.droidkaigi.confsched2017.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import io.github.droidkaigi.confsched2017.view.activity.MainActivity
import io.github.droidkaigi.confsched2017.view.activity.SessionDetailActivity
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = DefaultPrefs.get(context)
        if (!prefs.notificationFlag) {
            Timber.tag(TAG).v("Notification is disabled.")
            return
        }

        showGroupNotification(context)

        showChildNotification(context, intent, prefs.headsUpFlag)
    }

    /**
     * Show group notification
     * @param context Context
     */
    private fun showGroupNotification(context: Context) {
        // Group notification is supported on Android N and Android Wear.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val groupNotification = NotificationCompat.Builder(context, "")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                    .setColor(ContextCompat.getColor(context, R.color.theme))
                    .setAutoCancel(true)
                    .setGroup(GROUP_NAME)
                    .setGroupSummary(true)
                    .build()
            NotificationManagerCompat.from(context).notify(GROUP_NOTIFICATION_ID, groupNotification)
        }
    }

    /**
     * Show child notification
     * @param context Context
     * *
     * @param intent Intent
     */
    private fun showChildNotification(context: Context, intent: Intent, headsUp: Boolean) {
        val sessionId = intent.getIntExtra(KEY_SESSION_ID, 0)
        val title = intent.getStringExtra(KEY_TITLE)
        val text = intent.getStringExtra(KEY_TEXT)
        val priority = if (headsUp) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT
        val openIntent = SessionDetailActivity.createIntent(context, sessionId, MainActivity::class.java)
        openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, "")
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(context, R.color.theme))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(priority)
        // Group notification is supported on Android N and Android Wear.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setGroup(GROUP_NAME).setGroupSummary(false)
        }
        NotificationManagerCompat.from(context).notify(sessionId, builder.build())
    }

    companion object {

        private val TAG = NotificationReceiver::class.java.simpleName

        const val KEY_SESSION_ID = "session_id"

        const val KEY_TITLE = "title"

        const val KEY_TEXT = "text"

        const val GROUP_NAME = "droidkaigi"
        const val GROUP_NOTIFICATION_ID = 0

        fun createIntent(context: Context, sessionId: Int, title: String, text: String): Intent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.putExtra(NotificationReceiver.KEY_SESSION_ID, sessionId)
            intent.putExtra(NotificationReceiver.KEY_TITLE, title)
            intent.putExtra(NotificationReceiver.KEY_TEXT, text)
            return intent
        }
    }
}
