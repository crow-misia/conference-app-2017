package io.github.droidkaigi.confsched2017.view.activity

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.view.View

import java.util.Locale
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.github.droidkaigi.confsched2017.util.FpsMeasureUtil
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var sessionsRepository: SessionsRepository

    @Inject
    lateinit var mySessionsRepository: MySessionsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById<View>(android.R.id.content).systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onStart() {
        super.onStart()
        loadSessionsForCache()

        // Starting new Activity normally will not destroy this Activity, so set this up in start/stop cycle
        FpsMeasureUtil.play(application)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()

        // Stop tracking the frame rate.
        FpsMeasureUtil.finish()
    }

    private fun loadSessionsForCache() {
        Single.zip(
                sessionsRepository.findAll(Locale.getDefault()),
                mySessionsRepository.findAll(),
                Single.timer(MINIMUM_LOADING_TIME.toLong(), TimeUnit.MILLISECONDS),
                Function3 { _: List<Session>, _: List<MySession>, _: Long -> Observable.empty<Any>() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (isFinishing) return@doFinally

                    startActivity(MainActivity.createIntent(this@SplashActivity))
                    finish()
                }
                .subscribeBy(
                    onSuccess = { Timber.tag(TAG).d("Succeeded in loading sessions.") },
                    onError = { Timber.tag(TAG).e(it, "Failed to load sessions.") }
                )
                .addTo(compositeDisposable)
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName

        const val MINIMUM_LOADING_TIME = 1500
    }
}
