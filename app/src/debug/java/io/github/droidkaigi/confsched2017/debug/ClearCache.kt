package io.github.droidkaigi.confsched2017.debug

import com.tomoima.debot.strategy.DebotStrategy

import android.app.Activity
import android.widget.Toast

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class ClearCache @Inject
constructor(internal var sessionsRepository: SessionsRepository) : DebotStrategy() {

    override fun startAction(activity: Activity) {
        Completable.fromAction { sessionsRepository.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy { Toast.makeText(activity.applicationContext, "Cache Cleared", Toast.LENGTH_LONG).show() }
    }
}
