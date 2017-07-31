package io.github.droidkaigi.confsched2017.repository.sessions

import android.support.annotation.VisibleForTesting

import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Locale

import javax.inject.Inject
import javax.inject.Singleton

import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleEmitter

@Singleton
class SessionsRepository @Inject constructor(private val localDataSource: SessionsLocalDataSource, private val remoteDataSource: SessionsRemoteDataSource) : SessionsDataSource {

    @VisibleForTesting internal var cachedSessions: Map<Int, Session> = LinkedHashMap()

    private var isDirty = true

    override fun findAll(locale: Locale): Single<List<Session>> {
        if (hasCacheSessions()) {
            return Single.create { emitter -> emitter.onSuccess(ArrayList(cachedSessions.values)) }
        }

        if (isDirty) {
            return findAllFromRemote(locale)
        } else {
            return findAllFromLocal(locale)
        }
    }

    override fun find(sessionId: Int, locale: Locale): Maybe<Session> {
        if (hasCacheSession(sessionId)) {
            return Maybe.create { emitter -> emitter.onSuccess(cachedSessions[sessionId]!!) }
        }

        if (isDirty) {
            return remoteDataSource.find(sessionId, locale)
        } else {
            return localDataSource.find(sessionId, locale)
        }
    }

    override fun updateAllAsync(sessions: List<Session>) {
        localDataSource.updateAllAsync(sessions)
    }

    /**
     * Clear all caches. only for debug purposes
     */
    override fun deleteAll() {
        cachedSessions = LinkedHashMap()
        localDataSource.deleteAll()
        isDirty = true
    }

    private fun findAllFromLocal(locale: Locale): Single<List<Session>> {
        return localDataSource.findAll(locale)
                .flatMap { sessions ->
                    if (sessions.isEmpty()) {
                        return@flatMap findAllFromRemote(locale)
                    } else {
                        refreshCache(sessions)
                        return@flatMap Single.create<List<Session>> { it.onSuccess(sessions) }
                    }
                }
    }

    private fun findAllFromRemote(locale: Locale): Single<List<Session>> {
        return remoteDataSource.findAll(locale)
                .doOnSuccess { sessions ->
                    refreshCache(sessions)
                    updateAllAsync(sessions)
                }
    }

    private fun refreshCache(sessions: List<Session>) {
        cachedSessions = sessions.map { Pair(it.id, it) }.toMap()
        isDirty = false
    }

    fun setIdDirty(isDirty: Boolean) {
        this.isDirty = isDirty
    }

    internal fun hasCacheSessions(): Boolean {
        return !cachedSessions.isEmpty() && !isDirty
    }

    internal fun hasCacheSession(sessionId: Int): Boolean {
        return cachedSessions.containsKey(sessionId) && !isDirty
    }
}
