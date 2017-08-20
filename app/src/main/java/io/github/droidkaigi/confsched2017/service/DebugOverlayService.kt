package io.github.droidkaigi.confsched2017.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.service.helper.OverlayViewManager
import io.github.droidkaigi.confsched2017.util.SettingsUtil
import io.github.droidkaigi.confsched2017.view.activity.MainActivity
import timber.log.Timber

/**
 * Created by KeishinYokomaku on 2017/02/12.
 */

class DebugOverlayService : BaseService() {
    private val configurationChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handler.post {
                Timber.d("onConfigurationChange")
                manager.changeConfiguration()
            }
        }
    }

    @Inject
    lateinit var handler: Handler

    @Inject
    lateinit var manager: OverlayViewManager

    override fun onCreate() {
        super.onCreate()
        if (!SettingsUtil.canDrawOverlays(this)) {
            return
        }
        component.inject(this)
        Timber.d("onCreate")

        val filter = IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED)
        registerReceiver(configurationChangeReceiver, filter)
        manager.create()
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, @ServiceFlags flags: Int, startId: Int): Int {
        Timber.d("onStartCommand")
        return Service.START_NOT_STICKY // no need to restart a process
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Timber.d("onTaskRemoved")
        if (MainActivity::class.java.name == rootIntent?.component?.className) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        if (SettingsUtil.canDrawOverlays(this)) {
            Timber.d("onDestroy")
            manager.destroy()
            unregisterReceiver(configurationChangeReceiver)
        }
        super.onDestroy()
    }
}
