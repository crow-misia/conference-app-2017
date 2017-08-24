package io.github.droidkaigi.confsched2017.repository.sessions

import java.util.Locale

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.github.droidkaigi.confsched2017.model.Session
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SessionsLocalDataSource @Inject
constructor(private val orma: OrmaDatabase) : SessionsDataSource {

    private fun sessionRelation() = orma.relationOfSession()

    private fun speakerRelation() = orma.relationOfSpeaker()

    private fun placeRelation() = orma.relationOfRoom()

    private fun topicRelation() = orma.relationOfTopic()

    override fun findAll(locale: Locale): Single<List<Session>> = sessionRelation().selector().executeAsObservable().toList()

    override fun find(sessionId: Int, locale: Locale): Maybe<Session> = sessionRelation().selector().idEq(sessionId).executeAsObservable().firstElement()

    override fun deleteAll() {
        sessionRelation().deleter().execute()
        speakerRelation().deleter().execute()
        topicRelation().deleter().execute()
        placeRelation().deleter().execute()

    }

    private fun updateAllSync(sessions: List<Session>) {
        val relation = sessionRelation()

        for (session in sessions) {
            relation.upsert(session)
        }
    }

    override fun updateAllAsync(sessions: List<Session>) {
        orma.transactionAsCompletable { updateAllSync(sessions) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}
