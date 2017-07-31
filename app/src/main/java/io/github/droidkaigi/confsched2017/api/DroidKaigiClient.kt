package io.github.droidkaigi.confsched2017.api

import java.util.Locale

import javax.inject.Inject
import javax.inject.Singleton

import io.github.droidkaigi.confsched2017.api.service.DroidKaigiService
import io.github.droidkaigi.confsched2017.api.service.GithubService
import io.github.droidkaigi.confsched2017.api.service.GoogleFormService
import io.github.droidkaigi.confsched2017.model.Contributor
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.model.SessionFeedback
import io.reactivex.Single
import retrofit2.Response

@Singleton
class DroidKaigiClient @Inject constructor(private val droidKaigiService: DroidKaigiService, private val githubService: GithubService,
            private val googleFormService: GoogleFormService) {

    fun getSessions(locale: Locale?): Single<List<Session>> {
        return when(locale) {
            Locale.JAPANESE -> droidKaigiService.sessionsJa()
            else -> droidKaigiService.sessionsEn()
        }
    }

    fun contributors(): Single<List<Contributor>> {
        return githubService.getContributors("DroidKaigi", "conference-app-2017", INCLUDE_ANONYMOUS, MAX_PER_PAGE)
    }

    fun submitSessionFeedback(sessionFeedback: SessionFeedback): Single<Response<Void>> {
        return googleFormService.submitSessionFeedback(sessionFeedback.sessionId,
                sessionFeedback.sessionTitle,
                sessionFeedback.relevancy,
                sessionFeedback.asExpected,
                sessionFeedback.difficulty,
                sessionFeedback.knowledgeable,
                sessionFeedback.comment)
    }

    companion object {
        const val INCLUDE_ANONYMOUS = 1

        const val MAX_PER_PAGE = 100
    }
}
