package io.github.droidkaigi.confsched2017.repository.sessions

import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySessionsRepository @Inject constructor(localDataSource: MySessionsLocalDataSource) : MySessionsDataSource {

    private val localDataSource: MySessionsDataSource = localDataSource

    private val cachedMySessions = LinkedHashMap<Int, MySession>()

    override fun findAll(): Single<List<MySession>> {
        if (cachedMySessions.isNotEmpty()) {
            return Single.create<List<MySession>> { it.onSuccess(ArrayList(cachedMySessions.values)) }
        }

        return localDataSource.findAll().doOnSuccess { this.refreshCache(it) }
    }

    override fun save(session: Session): Completable {
        cachedMySessions.put(session.id, MySession(0, session))
        return localDataSource.save(session)
    }

    override fun delete(session: Session): Single<Int> {
        cachedMySessions.remove(session.id)
        return localDataSource.delete(session)
    }

    override fun isExist(sessionId: Int) = localDataSource.isExist(sessionId)

    private fun refreshCache(mySessions: List<MySession>) {
        cachedMySessions.clear()
        mySessions.forEach { cachedMySessions.put(it.session.id, it) }
    }
}
