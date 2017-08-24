package io.github.droidkaigi.confsched2017.viewmodel

import com.android.databinding.library.baseAdapters.BR

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View

import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.model.SessionFeedback
import io.github.droidkaigi.confsched2017.repository.feedbacks.SessionFeedbackRepository
import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SessionFeedbackViewModel @Inject internal constructor(
        private val sessionsRepository: SessionsRepository,
        private val sessionFeedbackRepository: SessionFeedbackRepository,
        private val navigator: Navigator,
        private val compositeDisposable: CompositeDisposable) : BaseObservable(), ViewModel {

    var session: Session? = null

    private var sessionTitle: String? = null

    @get:Bindable
    var relevancy: Int = 0
        set(relevancy) {
            field = relevancy
            sessionFeedback?.let {
                if (it.relevancy != relevancy) {
                    it.relevancy = relevancy
                }
            }
            notifyPropertyChanged(BR.relevancy)
        }

    @get:Bindable
    var asExpected: Int = 0
        set(asExpected) {
            field = asExpected
            sessionFeedback?.let {
                if (it.asExpected != asExpected) {
                    it.asExpected = asExpected
                }
            }
            notifyPropertyChanged(BR.asExpected)
        }

    @get:Bindable
    var difficulty: Int = 0
        set(difficulty) {
            field = difficulty
            sessionFeedback?.let {
                if (it.difficulty != difficulty) {
                    it.difficulty = difficulty
                }
            }
            notifyPropertyChanged(BR.difficulty)
        }

    @get:Bindable
    var knowledgeable: Int = 0
        set(knowledgeable) {
            field = knowledgeable
            sessionFeedback?.let {
                if (it.knowledgeable != knowledgeable) {
                    it.knowledgeable = knowledgeable
                }
            }
            notifyPropertyChanged(BR.knowledgeable)
        }

    @get:Bindable
    var comment: String? = null
        set(comment) {
            field = comment
            sessionFeedback?.let {
                if (comment != null && comment != it.comment) {
                    it.comment = comment
                }
            }
            notifyPropertyChanged(BR.comment)
        }

    @get:Bindable
    var loadingVisibility = View.GONE
        set(loadingVisibility) {
            field = loadingVisibility
            notifyPropertyChanged(BR.loadingVisibility)
        }

    @get:Bindable
    var isSubmitButtonEnabled = true
        set(submitButtonEnabled) {
            field = submitButtonEnabled
            notifyPropertyChanged(BR.submitButtonEnabled)
        }

    var callback: Callback? = null

    private var sessionFeedback: SessionFeedback? = null

    fun findSession(sessionId: Int) {
        sessionsRepository.find(sessionId, Locale.getDefault())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { this.initSessionFeedback(it) },
                        onError = { Timber.tag(TAG).e(it, "Failed to find session.") }
                ).addTo(compositeDisposable)
    }

    private fun initSessionFeedback(session: Session) {
        this.session = session
        this.sessionTitle = session.title
        notifyPropertyChanged(BR.sessionTitle)

        val sessionFeedback = sessionFeedbackRepository.findFromCache(session.id) ?:
            SessionFeedback.create(session, this.relevancy, this.asExpected, this.difficulty, this.knowledgeable, this.comment)

        this.sessionFeedback = sessionFeedback
        relevancy = sessionFeedback.relevancy
        asExpected = sessionFeedback.asExpected
        difficulty = sessionFeedback.difficulty
        knowledgeable = sessionFeedback.knowledgeable
        comment = sessionFeedback.comment

        isSubmitButtonEnabled = !sessionFeedback.isSubmitted

        callback?.onSessionFeedbackInitialized(sessionFeedback)
    }

    override fun destroy() {
        sessionFeedback?.let { sessionFeedbackRepository.saveToCache(it) }
        compositeDisposable.clear()
        this.callback = null
    }

    @Bindable
    fun getSessionTitle() = sessionTitle ?: ""

    fun setSessionTitle(sessionTitle: String) {
        this.sessionTitle = sessionTitle
        notifyPropertyChanged(BR.sessionTitle)
    }

    fun onClickSubmitFeedbackButton(view: View) {
        if (sessionFeedback?.isAllFilled ?: false) {
            navigator.showConfirmDialog(R.string.session_feedback_confirm_title,
                    R.string.session_feedback_confirm_message,
                    object : Navigator.ConfirmDialogListener {
                        override fun onClickPositiveButton() {
                            sessionFeedback?.let{ submit(it) }
                        }

                        override fun onClickNegativeButton() {
                            // Do nothing
                        }
                    })
        } else {
            callback?.onErrorUnFilled()
        }
    }

    private fun submit(sessionFeedback: SessionFeedback) {
        loadingVisibility = View.VISIBLE
        isSubmitButtonEnabled = false

        sessionFeedbackRepository.submit(sessionFeedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            loadingVisibility = View.GONE
                            sessionFeedback.isSubmitted = true
                            callback?.onSuccessSubmit()
                        }, onError = {
                            loadingVisibility = View.GONE
                            isSubmitButtonEnabled = true
                            callback?.onErrorSubmit()
                        }
                ).addTo(compositeDisposable)
    }

    interface Callback {
        fun onSuccessSubmit()

        fun onErrorSubmit()

        fun onErrorUnFilled()

        fun onSessionFeedbackInitialized(sessionFeedback: SessionFeedback)
    }

    companion object {
        private val TAG = SessionFeedbackViewModel::class.java.simpleName
    }
}
