package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.view.View

import java.util.Date

import io.github.droidkaigi.confsched2017.BR
import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.util.AlarmUtil
import io.github.droidkaigi.confsched2017.util.DateUtil
import io.github.droidkaigi.confsched2017.view.activity.MainActivity
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class SessionViewModel : BaseObservable, ViewModel {

    private val session: Session

    val shortStime: String

    val formattedDate: String

    val title: String

    val speakerName: String

    val roomName: String

    val languageId: String

    val minutes: String

    var rowSpan = 1
        private set

    var colSpan = 1
        private set

    var titleMaxLines = 3
        private set

    var speakerNameMaxLines = 1
        private set

    var backgroundResId: Int = 0
        @DrawableRes get
        @DrawableRes private set

    var topicColorResId: Int = 0
        @ColorRes get
        @ColorRes private set

    var isClickable: Boolean = false
        private set

    private var checkVisibility: Int = 0

    var normalSessionItemVisibility: Int = 0
        private set

    val languageVisibility: Int

    private val navigator: Navigator?

    private val mySessionsRepository: MySessionsRepository?

    internal constructor(session: Session, context: Context, navigator: Navigator, roomCount: Int, isMySession: Boolean,
                         mySessionsRepository: MySessionsRepository) {
        this.session = session
        this.navigator = navigator
        this.shortStime = DateUtil.getHourMinute(session.stime)
        this.formattedDate = DateUtil.getMonthDate(session.stime, context)
        this.title = session.title
        this.speakerName = session.speaker?.name ?: ""
        this.roomName = session.room?.name ?: ""
        this.languageId = session.lang?.toUpperCase() ?: ""
        this.languageVisibility = if (session.lang != null) View.VISIBLE else View.GONE

        this.minutes = context.getString(R.string.session_minutes, session.durationMin)

        decideRowSpan(session)
        this.colSpan = decideColSpan(session, roomCount)

        this.checkVisibility = if (isMySession) View.VISIBLE else View.GONE

        if (session.isBreak) {
            this.isClickable = false
            this.backgroundResId = R.drawable.bg_empty_session
            this.topicColorResId = android.R.color.transparent
        } else {
            this.isClickable = true
            this.backgroundResId = if (session.isLiveAt(Date())) R.drawable.clickable_purple else R.drawable.clickable_white
            this.topicColorResId = TopicColor.from(session.topic).middleColorResId
        }

        this.normalSessionItemVisibility = if (!session.isBreak && !session.isDinner) View.VISIBLE else View.GONE

        this.mySessionsRepository = mySessionsRepository
    }

    private constructor(rowSpan: Int, colSpan: Int) {
        this.session = Session(title = "", stime = Date(0), etime = Date(0), durationMin = 0, type = "")
        this.rowSpan = rowSpan
        this.colSpan = colSpan
        this.isClickable = false
        this.backgroundResId = R.drawable.bg_empty_session
        this.topicColorResId = android.R.color.transparent
        this.checkVisibility = View.GONE
        this.normalSessionItemVisibility = View.GONE

        this.shortStime = ""
        this.formattedDate = ""
        this.title = ""
        this.speakerName = ""
        this.roomName = ""
        this.languageId = ""
        this.minutes = ""
        this.languageVisibility = View.GONE
        this.navigator = null
        this.mySessionsRepository = null
    }

    private fun decideColSpan(session: Session, roomCount: Int): Int {
        if (session.isCeremony) {
            return 3
        } else if (session.isBreak || session.isDinner) {
            return roomCount
        } else {
            return 1
        }
    }

    private fun decideRowSpan(session: Session) {
        // Break time is over 30 min, but one row is good
        if (session.durationMin > 30 && !session.isBreak) {
            this.rowSpan = this.rowSpan * 2
            this.titleMaxLines = this.titleMaxLines * 2
            this.speakerNameMaxLines = this.speakerNameMaxLines * 3
        }
    }

    internal val stime: Date
        get() = session.stime

    fun showSessionDetail(view: View) {
        navigator?.navigateToSessionDetail(session, MainActivity::class)
    }

    fun checkSession(view: View): Boolean {
        mySessionsRepository?: return false

        if (mySessionsRepository.isExist(session.id)) {
            mySessionsRepository.delete(session)
                    .subscribeBy(
                            onSuccess = {
                                setCheckVisibility(View.GONE)
                                AlarmUtil.unregisterAlarm(view.context, session)
                            },
                            onError = { Timber.tag(TAG).e(it, "Failed to delete my session") }
                    )
        } else {
            mySessionsRepository.save(session)
                    .subscribeBy(
                            onComplete = {
                                setCheckVisibility(View.VISIBLE)
                                AlarmUtil.registerAlarm(view.context, session)
                            },
                            onError = { Timber.tag(TAG).e(it, "Failed to save my session") }
                    )
        }
        return true
    }

    override fun destroy() {
        // Nothing to do
    }

    @Bindable
    fun getCheckVisibility() = checkVisibility

    private fun setCheckVisibility(visibility: Int) {
        checkVisibility = visibility
        notifyPropertyChanged(BR.checkVisibility)
    }

    companion object {
        private val TAG = SessionViewModel::class.java.simpleName

        @JvmOverloads internal fun createEmpty(rowSpan: Int, colSpan: Int = 1) = SessionViewModel(rowSpan, colSpan)
    }
}
