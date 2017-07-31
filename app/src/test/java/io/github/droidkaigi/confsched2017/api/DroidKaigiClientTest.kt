package io.github.droidkaigi.confsched2017.api

import com.nhaarman.mockito_kotlin.*
import io.github.droidkaigi.confsched2017.api.service.DroidKaigiService
import io.github.droidkaigi.confsched2017.api.service.GithubService
import io.github.droidkaigi.confsched2017.api.service.GoogleFormService
import io.github.droidkaigi.confsched2017.util.DummyCreator
import io.reactivex.Single
import org.junit.Test
import java.util.*

class DroidKaigiClientTest {

    private val droidKaigiService = mock<DroidKaigiService>()

    private val githubService = mock<GithubService>()

    private val googleFormService = mock<GoogleFormService>()

    private val client = DroidKaigiClient(droidKaigiService, githubService, googleFormService)

    @Test
    @Throws(Exception::class)
    fun getSessions() {
        val expected = Array(10) { DummyCreator.newSession(it) }.toList()
        whenever(droidKaigiService.sessionsJa()) doReturn Single.just(expected)
        whenever(droidKaigiService.sessionsEn()) doReturn Single.just(expected)

        client.getSessions(Locale.JAPANESE).test().run {
            assertNoErrors()
            assertResult(expected)
            assertComplete()
        }

        client.getSessions(Locale.ENGLISH).test().run {
            assertNoErrors()
            assertResult(expected)
            assertComplete()
        }
        verify(droidKaigiService, times(1)).sessionsJa()
        verify(droidKaigiService, times(1)).sessionsEn()
    }

    @Test
    @Throws(Exception::class)
    fun getContributors() {
        val expected = Array(10) { DummyCreator.newContributor(it) }.toList()
        whenever(githubService.getContributors("DroidKaigi", "conference-app-2017", 1, 100)) doReturn Single.just(expected)

        client.contributors().test().run {
            assertNoErrors()
            assertResult(expected)
            assertComplete()
        }
    }
}
