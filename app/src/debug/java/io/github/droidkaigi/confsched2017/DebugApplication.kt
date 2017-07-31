package io.github.droidkaigi.confsched2017

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
}
