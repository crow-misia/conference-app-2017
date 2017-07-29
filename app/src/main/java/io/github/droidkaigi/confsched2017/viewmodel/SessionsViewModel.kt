package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils
import android.view.View

import java.util.ArrayList
import java.util.Date
import java.util.LinkedHashMap
import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.BR
import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.Room
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.github.droidkaigi.confsched2017.util.DateUtil
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SessionsViewModel @Inject internal constructor(private val navigator: Navigator, private val sessionsRepository: SessionsRepository, private val mySessionsRepository: MySessionsRepository) : BaseObservable(), ViewModel {

    var rooms: List<Room>? = null
        private set

    var stimes: List<Date> = emptyList()
        private set

    override fun destroy() {
        // Do nothing
    }

    fun getSessions(locale: Locale, context: Context): Single<List<SessionViewModel>> {
        return Single.zip(
                sessionsRepository.findAll(locale),
                mySessionsRepository.findAll(),
                BiFunction { sessions, mySessions ->
                    val mySessionMap = LinkedHashMap<Int, MySession>()
                    mySessions.forEach { mySessionMap.put(it.session.id, it) }

                    this.rooms = extractRooms(sessions)
                    this.stimes = extractStimes(sessions)

                    notifyPropertyChanged(BR.loadingVisibility)

                    val viewModels = sessions
                            .map {
                                val isMySession = mySessionMap.containsKey(it.id)
                                SessionViewModel(it, context, navigator, rooms?.size ?: 0, isMySession, mySessionsRepository)
                            }
                            .toList()
                    adjustViewModels(viewModels, context)
                })
    }

    private fun adjustViewModels(sessionViewModels: List<SessionViewModel>, context: Context): List<SessionViewModel> {
        // Prepare sessions map
        val sessionMap = LinkedHashMap<String, SessionViewModel>()
        for (viewModel in sessionViewModels) {
            var roomName = viewModel.roomName
            if (TextUtils.isEmpty(roomName)) {
                // In the case of Welcome talk and lunch time, set dummy room
                roomName = rooms?.get(0)?.name ?: ""
            }
            sessionMap.put(generateStimeRoomKey(viewModel.stime, roomName), viewModel)
        }

        val adjustedViewModels = ArrayList<SessionViewModel>()

        // Format date that user can see. Ex) 9, March
        var lastFormattedDate: String? = null
        for (stime in stimes) {
            if (lastFormattedDate == null) {
                lastFormattedDate = DateUtil.getMonthDate(stime, context)
            }

            val sameTimeViewModels = ArrayList<SessionViewModel>()
            var maxRowSpan = 1
            var i = 0
            val size = rooms?.size ?: 0
            while (i < size) {
                val room = rooms?.get(i)
                room?.let{ sessionMap[generateStimeRoomKey(stime, it.name)] }?.let {
                    if (lastFormattedDate != it.formattedDate) {
                        // Change the date
                        lastFormattedDate = it.formattedDate
                        // Add empty row which divides the days
                        adjustedViewModels.add(SessionViewModel.createEmpty(1, rooms?.size ?: 0))
                    }
                    sameTimeViewModels.add(it)

                    if (it.rowSpan > maxRowSpan) {
                        maxRowSpan = it.rowSpan
                    }

                    var j = 1
                    val colSize = it.colSpan
                    while (j < colSize) {
                        // If the col size is over 1, skip next loop.
                        i++
                        j++
                    }
                } ?: run {
                    val empty = SessionViewModel.createEmpty(1)
                    sameTimeViewModels.add(empty)
                }
                i++
            }

            val copiedTmpViewModels = ArrayList(sameTimeViewModels)
            sameTimeViewModels
                    .asSequence()
                    .map { it.rowSpan }
                    .filter { it < maxRowSpan }
                    // Fill for empty cell
                   .mapTo(copiedTmpViewModels) { SessionViewModel.createEmpty(maxRowSpan - it) }

            adjustedViewModels.addAll(copiedTmpViewModels)
        }

        return adjustedViewModels
    }

    private fun generateStimeRoomKey(stime: Date, roomName: String) = DateUtil.getLongFormatDate(stime) + "_" + roomName

    private fun extractStimes(sessions: List<Session>) =
        sessions.asSequence()
                .map { it.stime }
                .sorted()
                .distinct()
                .toList()

    private fun extractRooms(sessions: List<Session>) =
        sessions.asSequence()
                .map { it.room }
                .filterNotNull()
                .filter { it.id != 0 }
                .sortedWith(Comparator { lhs, rhs -> lhs.name.compareTo(rhs.name) })
                .distinct()
                .toList()

    val loadingVisibility: Int
        @Bindable
        get() {
            if (this.rooms == null) {
                return View.VISIBLE
            } else {
                return View.GONE
            }
        }
}
