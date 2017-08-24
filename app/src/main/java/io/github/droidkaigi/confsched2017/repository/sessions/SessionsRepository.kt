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

@Singleton
class SessionsRepository @Inject constructor(private val localDataSource: SessionsLocalDataSource, private val remoteDataSource: SessionsRemoteDataSource) : SessionsDataSource {

    @VisibleForTesting internal var cachedSessions: MutableMap<Int, Session> = LinkedHashMap()

    private var isDirty = true

    override fun findAll(locale: Locale): Single<List<Session>> {
        if (hasCacheSessions()) {
            return Single.create { it.onSuccess(ArrayList(cachedSessions.values)) }
        }

        if (isDirty) {
            return findAllFromRemote(locale)
        }
        return findAllFromLocal(locale)
    }

    override fun find(sessionId: Int, locale: Locale): Maybe<Session> {
        if (hasCacheSession(sessionId)) {
            return Maybe.create { it.onSuccess(cachedSessions[sessionId]!!) }
        }

        if (isDirty) {
            return remoteDataSource.find(sessionId, locale)
        }
        return localDataSource.find(sessionId, locale)
    }

    override fun updateAllAsync(sessions: List<Session>) {
        localDataSource.updateAllAsync(sessions)
    }

    /**
     * Clear all caches. only for debug purposes
     */
    override fun deleteAll() {
        cachedSessions.clear()
        localDataSource.deleteAll()
        isDirty = true
    }

    private fun findAllFromLocal(locale: Locale): Single<List<Session>> {
        return localDataSource.findAll(locale)
                .flatMap { sessions ->
                    if (sessions.isEmpty()) {
                        return@flatMap findAllFromRemote(locale)
                    }
                    refreshCache(sessions)
                    return@flatMap Single.create<List<Session>> { it.onSuccess(sessions) }
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
        cachedSessions.clear()
        sessions.forEach { cachedSessions.put(it.id, it) }
        isDirty = false
    }

    fun setIdDirty(isDirty: Boolean) {
        this.isDirty = isDirty
    }

    internal fun hasCacheSessions() = cachedSessions.isNotEmpty() && !isDirty

    internal fun hasCacheSession(sessionId: Int) = cachedSessions.containsKey(sessionId) && !isDirty
}
