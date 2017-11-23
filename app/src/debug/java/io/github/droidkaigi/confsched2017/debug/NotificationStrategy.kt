package io.github.droidkaigi.confsched2017.debug

import com.tomoima.debot.strategy.DebotStrategy

import android.app.Activity
import android.widget.Toast

import io.github.droidkaigi.confsched2017.MainApplication
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import javax.inject.Inject

class NotificationStrategy @Inject internal constructor() : DebotStrategy() {

    override fun startAction(activity: Activity) {
        if (DefaultPrefs.notificationTestFlag) {
            DefaultPrefs.notificationTestFlag = false
            Toast.makeText(activity, "Notification will be displayed 10 minutes before sessions", Toast.LENGTH_SHORT).show()
        } else {
            DefaultPrefs.notificationTestFlag = true
            Toast.makeText(activity, "Notification will be displayed after 5 seconds", Toast.LENGTH_SHORT).show()
        }

        val application = activity.application as MainApplication
        application.initDebot()
    }
}
