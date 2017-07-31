package io.github.droidkaigi.confsched2017.debug

import com.tomoima.debot.strategy.DebotStrategy

import android.app.Activity

import javax.inject.Inject


class ClearCache @Inject
constructor() : DebotStrategy() {

    override fun startAction(activity: Activity) {
        // Do nothing
    }
}
