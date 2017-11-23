package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.text.TextUtils
import android.view.View

import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.github.droidkaigi.confsched2017.util.AlarmUtil
import io.github.droidkaigi.confsched2017.util.DateUtil
import io.github.droidkaigi.confsched2017.util.LocaleUtil
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class SessionDetailViewModel @Inject constructor(
        private val context: Context, private val navigator: Navigator, private val sessionsRepository: SessionsRepository,
        private val mySessionsRepository: MySessionsRepository) : BaseObservable(), ViewModel {

    var sessionTitle: String? = null
        private set

    var speakerImageUrl: String? = null
        private set

    var sessionVividColorResId = R.color.white
        @ColorRes get
        @ColorRes private set

    var sessionPaleColorResId = R.color.white
        @ColorRes get
        @ColorRes private set

    var topicThemeResId = R.color.white
        @StyleRes get
        @StyleRes private set

    var languageResId = R.string.lang_en
        @StringRes get
        @StringRes private set

    var sessionTimeRange: String? = null
        private set

    var session: Session? = null
        private set(value) {
            value?.let {
                field = it
                sessionTitle = it.title

                speakerImageUrl = it.speaker?.adjustedImageUrl

                val topicColor = TopicColor.from(it.topic)
                sessionVividColorResId = topicColor.vividColorResId
                sessionPaleColorResId = topicColor.paleColorResId
                topicThemeResId = topicColor.themeId
                sessionTimeRange = decideSessionTimeRange(context, it)
                isMySession = mySessionsRepository.isExist(it.id)
                tagContainerVisibility = if (!it.isDinner) View.VISIBLE else View.GONE
                speakerVisibility = if (!it.isDinner) View.VISIBLE else View.GONE
                slideIconVisibility = if (it.slideUrl != null) View.VISIBLE else View.GONE
                dashVideoIconVisibility = if (it.movieUrl != null && it.movieDashUrl != null) View.VISIBLE else View.GONE
                roomVisibility = if (it.room != null) View.VISIBLE else View.GONE
                topicVisibility = if (it.topic != null) View.VISIBLE else View.GONE
                feedbackButtonVisiblity = if (!it.isDinner) View.VISIBLE else View.GONE
                languageResId = it.lang?.let { decideLanguageResId(Locale(it.toLowerCase())) } ?: R.string.lang_en
            }
        }

    var isMySession: Boolean = false
        private set

    var tagContainerVisibility: Int = 0
        private set

    var speakerVisibility: Int = 0
        private set

    var slideIconVisibility: Int = 0
        private set

    var dashVideoIconVisibility: Int = 0
        private set

    var roomVisibility: Int = 0
        private set

    var topicVisibility: Int = 0
        private set

    var feedbackButtonVisiblity: Int = 0
        private set

    var callback: Callback? = null

    fun loadSession(sessionId: Int): Completable = sessionsRepository.find(sessionId, Locale.getDefault())
            .flatMapCompletable {
                session = it
                Completable.complete()
            }

    private fun decideLanguageResId(locale: Locale): Int = when (locale) {
        Locale.JAPANESE -> R.string.lang_ja
        else -> R.string.lang_en
    }

    override fun destroy() {
        this.callback = null
    }

    fun shouldShowShareMenuItem() = !TextUtils.isEmpty(session?.shareUrl)

    fun onClickShareMenuItem() {
        //
    }

    fun onClickFeedbackButton(view: View) {
        session?.let { navigator.navigateToFeedbackPage(it) }
    }

    fun onClickSlideIcon(view: View) {
        //        if (session.hasSlide()) {
        //        }
    }

    fun onClickMovieIcon(view: View) {
        //        if (session.hasDashVideo()) {
        //        }
    }

    fun onClickFab(view: View) {
        var selected = true
        session?.let {
            if (mySessionsRepository.isExist(it.id)) {
                selected = false
                mySessionsRepository.delete(it).subscribeBy(
                        onSuccess = { Timber.tag(TAG).d("Deleted my session") },
                        onError = { Timber.tag(TAG).e(it, "Failed to delete my session") }
                )
                AlarmUtil.unregisterAlarm(context, it)
            } else {
                selected = true
                mySessionsRepository.save(it).subscribeBy(
                        onComplete = { Timber.tag(TAG).d("Saved my session") },
                        onError = { Timber.tag(TAG).e(it, "Failed to save my session") }
                )
                AlarmUtil.registerAlarm(context, it)
            }
        }

        callback?.onClickFab(selected)
    }

    fun onOverScroll() {
        callback?.onOverScroll()
    }

    private fun decideSessionTimeRange(context: Context, session: Session): String {
        val displaySTime = LocaleUtil.getDisplayDate(session.stime)
        val displayETime = LocaleUtil.getDisplayDate(session.etime)

        return context.getString(R.string.session_time_range,
                DateUtil.getLongFormatDate(displaySTime),
                DateUtil.getHourMinute(displayETime),
                DateUtil.getMinutes(displaySTime, displayETime))
    }

    interface Callback {
        fun onClickFab(selected: Boolean)

        fun onOverScroll()
    }

    companion object {
        private val TAG = SessionDetailViewModel::class.java.simpleName
    }
}
