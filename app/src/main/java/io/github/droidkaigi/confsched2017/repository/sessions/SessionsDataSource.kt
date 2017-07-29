package io.github.droidkaigi.confsched2017.repository.sessions

import java.util.Locale

import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Maybe
import io.reactivex.Single

interface SessionsDataSource {

    fun findAll(locale: Locale): Single<List<Session>>

    fun find(sessionId: Int, locale: Locale): Maybe<Session>

    fun updateAllAsync(sessions: List<Session>)

    fun deleteAll()
}
