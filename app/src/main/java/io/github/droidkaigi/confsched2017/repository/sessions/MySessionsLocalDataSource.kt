package io.github.droidkaigi.confsched2017.repository.sessions

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.model.MySession
import io.github.droidkaigi.confsched2017.model.MySession_Relation
import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MySessionsLocalDataSource @Inject constructor(private val orma: OrmaDatabase) : MySessionsDataSource {

    private fun mySessionRelation() = orma.relationOfMySession()

    override fun findAll(): Single<List<MySession>> {
        // TODO
        if (mySessionRelation().isEmpty) {
            return Single.create { emitter -> emitter.onSuccess(emptyList()) }
        } else {
            return mySessionRelation().selector().executeAsObservable().toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun save(session: Session) =
        orma.transactionAsCompletable({
            mySessionRelation().deleter().sessionEq(session.id).execute()
            mySessionRelation().inserter().execute(MySession(0, session))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override fun delete(session: Session) =
        mySessionRelation().deleter().sessionEq(session.id).executeAsSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    override fun isExist(sessionId: Int) =
        !mySessionRelation().selector().sessionEq(sessionId).isEmpty()
}
