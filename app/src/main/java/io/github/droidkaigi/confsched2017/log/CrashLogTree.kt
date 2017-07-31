package io.github.droidkaigi.confsched2017.log

import com.google.firebase.crash.FirebaseCrash

import android.util.Log

import timber.log.Timber

/**
 * Created by KeishinYokomaku on 2017/01/18.
 */
class CrashLogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            FirebaseCrash.log((if (priority == Log.DEBUG) "[debug] " else "[verbose] ") + tag + ": " + message)
            return
        }
        FirebaseCrash.logcat(priority, tag, message)
        t?.let {
            if (priority == Log.ERROR || priority == Log.WARN) {
                FirebaseCrash.report(it)
            }
        }
    }
}
