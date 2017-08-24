package io.github.droidkaigi.confsched2017.repository.feedbacks

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.api.DroidKaigiClient
import io.github.droidkaigi.confsched2017.model.SessionFeedback

class SessionFeedbackRemoteDataSource @Inject constructor(private val client: DroidKaigiClient) {

    fun submit(sessionFeedback: SessionFeedback) = client.submitSessionFeedback(sessionFeedback)
}
