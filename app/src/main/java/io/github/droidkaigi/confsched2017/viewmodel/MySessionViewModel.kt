package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.view.View

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.util.DateUtil
import io.github.droidkaigi.confsched2017.util.LocaleUtil
import io.github.droidkaigi.confsched2017.view.activity.MySessionsActivity
import io.github.droidkaigi.confsched2017.view.helper.Navigator

class MySessionViewModel(context: Context, private val navigator: Navigator, var mySession: MySession) : BaseObservable(), ViewModel {

    val sessionTitle = mySession.session.title

    var speakerImageUrl = mySession.session.speaker?.adjustedImageUrl

    val sessionTimeRange = decideSessionTimeRange(context, mySession.session)

    val roomVisibility = if (mySession.session.room != null) View.VISIBLE else View.GONE

    override fun destroy() {
        // Nothing to do
    }

    private fun decideSessionTimeRange(context: Context, session: Session): String {
        val displaySTime = LocaleUtil.getDisplayDate(session.stime, context)
        val displayETime = LocaleUtil.getDisplayDate(session.etime, context)

        return context.getString(R.string.session_time_range,
                DateUtil.getLongFormatDate(displaySTime),
                DateUtil.getHourMinute(displayETime),
                DateUtil.getMinutes(displaySTime, displayETime))
    }

    fun onItemClick(view: View) = navigator.navigateToSessionDetail(mySession.session, MySessionsActivity::class)
}
