package io.github.droidkaigi.confsched2017.repository.feedbacks

import javax.inject.Inject
import javax.inject.Singleton

import io.github.droidkaigi.confsched2017.model.SessionFeedback
import io.reactivex.Single
import retrofit2.Response

@Singleton
class SessionFeedbackRepository @Inject constructor(
        private val remoteDataSource: SessionFeedbackRemoteDataSource,
        private val localDataSource: SessionFeedbackLocalDataSource) {

    fun submit(sessionFeedback: SessionFeedback) = remoteDataSource.submit(sessionFeedback)

    fun findFromCache(sessionId: Int) = localDataSource.find(sessionId)

    fun saveToCache(sessionFeedback: SessionFeedback) = localDataSource.save(sessionFeedback)
}
