package io.github.droidkaigi.confsched2017.repository.feedbacks

import com.github.gfx.android.orma.annotation.OnConflict

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.github.droidkaigi.confsched2017.model.SessionFeedback
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SessionFeedbackLocalDataSource @Inject constructor(private val orma: OrmaDatabase) {

    fun save(sessionFeedback: SessionFeedback): Disposable =
        orma.transactionAsCompletable { orma.prepareInsertIntoSessionFeedback(OnConflict.REPLACE).execute(sessionFeedback) }
                .subscribeOn(Schedulers.io())
                .subscribe()

    fun find(sessionId: Int) = orma.relationOfSessionFeedback().selector().sessionIdEq(sessionId).getOrNull(0)
}
