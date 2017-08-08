package io.github.droidkaigi.confsched2017

import android.content.Context
import android.support.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree

import timber.log.Timber

class DebugApplication : MainApplication() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        Timber.plant(StethoTree())
        Timber.plant(Timber.DebugTree())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(base)
    }
}
