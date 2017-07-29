package io.github.droidkaigi.confsched2017.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.view.View
import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository
import io.github.droidkaigi.confsched2017.repository.sessions.SessionsRepository
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.Single

class SearchViewModel @Inject constructor(private val navigator: Navigator, private val sessionsRepository: SessionsRepository, private val mySessionsRepository: MySessionsRepository) : BaseObservable(), ViewModel {

    var callback: Callback? = null

    override fun destroy() {
        this.callback = null
    }

    fun onClickCover(view: View) {
        callback?.closeSearchResultList()
    }

    fun getSearchResultViewModels(context: Context): Single<List<SearchResultViewModel>> {
        return sessionsRepository.findAll(Locale.getDefault())
                .map { sessions ->
                    val filteredSessions = sessions
                            .filter { it.isSession && it.speaker != null }
                            .toList()

                    return@map filteredSessions
                            .map { SearchResultViewModel.createTitleType(it, context, navigator, mySessionsRepository) }
                            .plus(filteredSessions.map { SearchResultViewModel.createDescriptionType(it, context, navigator, mySessionsRepository) })
                            .plus(filteredSessions.map { SearchResultViewModel.createSpeakerType(it, context, navigator, mySessionsRepository) })
                            .toList()
                }
    }

    interface Callback {
        fun closeSearchResultList()
    }
}
