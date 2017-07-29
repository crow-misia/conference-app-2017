package io.github.droidkaigi.confsched2017.repository.sessions

import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Completable
import io.reactivex.Single

interface MySessionsDataSource {

    fun findAll(): Single<List<MySession>>

    fun save(session: Session): Completable

    fun delete(session: Session): Single<Int>

    fun isExist(sessionId: Int): Boolean
}
