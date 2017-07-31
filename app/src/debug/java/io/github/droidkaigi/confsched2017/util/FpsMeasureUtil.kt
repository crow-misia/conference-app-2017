package io.github.droidkaigi.confsched2017.util

import android.app.Application

import jp.wasabeef.takt.Seat
import jp.wasabeef.takt.Takt
import timber.log.Timber

object FpsMeasureUtil {

    fun play(application: Application) {
        Takt.stock(application)
                .seat(Seat.BOTTOM_RIGHT)
                .interval(250)
                .listener { fps -> Timber.i("heartbeat() called with: fps = [ %1$.3f ms ]", fps) }
                .play()
    }

    fun finish() {
        Takt.finish()
    }
}
