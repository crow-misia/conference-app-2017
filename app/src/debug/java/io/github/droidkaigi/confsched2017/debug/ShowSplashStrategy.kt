package io.github.droidkaigi.confsched2017.debug

import com.tomoima.debot.strategy.DebotStrategy

import android.app.Activity
import android.content.Intent

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.view.activity.SplashViewActivity

class ShowSplashStrategy @Inject internal constructor() : DebotStrategy() {

    override fun startAction(activity: Activity) {
        activity.startActivity(Intent(activity, SplashViewActivity::class.java))
    }
}
