package io.github.droidkaigi.confsched2017.repository.sessions

import android.text.TextUtils
import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.api.DroidKaigiClient
import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class SessionsRemoteDataSource @Inject constructor(private val client: DroidKaigiClient) : SessionsDataSource {

    override fun findAll(locale: Locale): Single<List<Session>> {
        return client.getSessions(locale)
                .doOnSuccess { sessions ->
                    // API returns some sessions which have empty room info.
                    sessions.asSequence()
                            .filter { it.room != null && TextUtils.isEmpty(it.room?.name) }
                            .forEach { it.room = null }
                }
    }

    override fun find(sessionId: Int, locale: Locale): Maybe<Session> {
        return findAll(locale)
                .toObservable()
                .flatMap { Observable.fromIterable(it) }
                .filter { session -> session.id == sessionId }
                .singleElement()
    }

    override fun updateAllAsync(sessions: List<Session>) {
        // Do nothing
    }

    override fun deleteAll() {
        // Do nothing
    }
}
