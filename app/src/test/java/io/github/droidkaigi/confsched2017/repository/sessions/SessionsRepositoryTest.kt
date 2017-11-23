package io.github.droidkaigi.confsched2017.repository.sessions

import com.github.gfx.android.orma.AccessThreadConstraint
import com.nhaarman.mockito_kotlin.*
import io.github.droidkaigi.confsched2017.api.DroidKaigiClient
import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.util.DummyCreator
import io.reactivex.Completable
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class SessionsRepositoryTest {

    fun createOrmaDatabase(): OrmaDatabase {
        return OrmaDatabase.builder(RuntimeEnvironment.application)
                .name(null)
                .writeOnMainThread(AccessThreadConstraint.WARNING)
                .build()
    }

    @Test
    fun hasCacheSessions() {
        // false. cache is null.
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )

            assertThat(repository.hasCacheSessions()).isFalse()
        }

        // false. repository has any cached session, but repository is dirty.
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )
            repository.cachedSessions = mutableMapOf(0 to createSession(0))
            repository.setIdDirty(true)

            assertThat(repository.hasCacheSessions()).isFalse()
        }

        // true.
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )
            repository.cachedSessions = mutableMapOf(0 to createSession(0))
            repository.setIdDirty(false)

            assertThat(repository.hasCacheSessions()).isTrue()
        }
    }

    @Test
    fun findAllRemoteRequestAndLocalCache() {
        val sessions = listOf(createSession(0))
        val client = mockDroidKaigiClient(sessions)
        val ormaDatabase = mock<OrmaDatabase> {
            on { transactionAsCompletable(any()) } doReturn Completable.complete()
        }
        val cachedSessions: MutableMap<Int, Session> = spy(mutableMapOf())

        val repository = SessionsRepository(
                SessionsLocalDataSource(ormaDatabase),
                SessionsRemoteDataSource(client)
        ).apply {
            this.cachedSessions = cachedSessions
        }

        repository.findAll(Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertResult(sessions)
                    assertComplete()

                    verify(client).getSessions(eq(Locale.JAPANESE))
                    verify(ormaDatabase).transactionAsCompletable(any())
                    verify(cachedSessions, never()).values
                }

        repository.findAll(Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertThat(values().first().size).isEqualTo(1)
                    assertComplete()

                    verify(cachedSessions).values
                }
    }

    @Test
    fun findAllLocalCache() {
        val sessions = listOf(createSession(0))
        val client = mockDroidKaigiClient(sessions)
        val cachedSessions: MutableMap<Int, Session> = mock()

        val repository = SessionsRepository(
                SessionsLocalDataSource(createOrmaDatabase()),
                SessionsRemoteDataSource(client)
        ).apply {
            this.cachedSessions = cachedSessions
        }

        repository.findAll(Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertResult(sessions)
                    assertComplete()

                    verify(client).getSessions(eq(Locale.JAPANESE))
                    verify(cachedSessions, never()).values
                }

        repository.setIdDirty(true)

        repository.findAll(Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertThat(values().first().size).isEqualTo(1)
                    assertComplete()

                    verify(cachedSessions, never()).values
                }
    }

    @Test
    fun hasCacheSession() {
        // false cachedSessions is null
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )

            assertThat(repository.hasCacheSession(0)).isFalse()
        }

        // false sessionId not found
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )
            repository.cachedSessions = mutableMapOf(1 to createSession(0))
            repository.setIdDirty(false)

            assertThat(repository.hasCacheSession(0)).isFalse()
        }

        // false dirty
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )
            repository.cachedSessions = mutableMapOf(1 to createSession(0))
            repository.setIdDirty(true)

            assertThat(repository.hasCacheSession(1)).isFalse()
        }

        // true
        run {
            val repository = SessionsRepository(
                    SessionsLocalDataSource(createOrmaDatabase()),
                    SessionsRemoteDataSource(mock())
            )
            repository.cachedSessions = mutableMapOf(1 to createSession(0))
            repository.setIdDirty(false)

            assertThat(repository.hasCacheSession(1)).isTrue()
        }
    }

    @Test
    fun findRemoteRequestSuccess() {
        val sessions = listOf(createSession(2), createSession(3))
        val client = mockDroidKaigiClient(sessions)

        val repository = SessionsRepository(
                SessionsLocalDataSource(createOrmaDatabase()),
                SessionsRemoteDataSource(client)
        )

        repository.find(3, Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertThat(values().first().id).isEqualTo(3)
                    assertComplete()
                }
    }

    @Test
    fun findRemoteRequestNotFound() {
        val sessions = listOf(createSession(1), createSession(2))
        val client = mockDroidKaigiClient(sessions)

        val repository = SessionsRepository(
                SessionsLocalDataSource(createOrmaDatabase()),
                SessionsRemoteDataSource(client)
        )

        repository.find(3, Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertNoValues()
                    assertComplete()
                }
    }

    @Test
    fun findHasSessions() {
        val cachedSessions: MutableMap<Int, Session> = spy(mutableMapOf(1 to createSession(1)))

        val repository = SessionsRepository(
                SessionsLocalDataSource(createOrmaDatabase()),
                SessionsRemoteDataSource(mock())
        ).apply {
            this.cachedSessions = cachedSessions
        }

        repository.setIdDirty(false)
        repository.find(1, Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertThat(values().first().id).isEqualTo(1)
                    assertComplete()
                    verify(cachedSessions)[eq(1)]
                }
    }

    @Test
    fun findLocalStorage() {
        val ormaDatabase = createOrmaDatabase()
        val testSession = createSession(12)
        // room / speaker / topic create
        testSession.room?.let { ormaDatabase.insertIntoRoom(it) }
        testSession.speaker?.let { ormaDatabase.insertIntoSpeaker(it) }
        testSession.topic?.let { ormaDatabase.insertIntoTopic(it) }

        ormaDatabase
                .insertIntoSession(createSession(12).apply {
                    title = "awesome session"
                    stime = Date()
                    etime = Date()
                    durationMin = 30
                    type = "android"
                })

        val cachedSessions: MutableMap<Int, Session> = mock()
        val client: DroidKaigiClient = mock()
        val repository = SessionsRepository(
                SessionsLocalDataSource(ormaDatabase),
                SessionsRemoteDataSource(client)
        )

        repository.cachedSessions = cachedSessions
        repository.setIdDirty(false)
        repository.find(12, Locale.JAPANESE)
                .test()
                .run {
                    assertNoErrors()
                    assertThat(values().first().id).isEqualTo(12)
                    assertComplete()
                    verify(cachedSessions, never()).get(eq(12))
                    verify(client, never()).getSessions(any())
                }
    }

    fun createSession(sessionId: Int) = DummyCreator.newSession(sessionId)

    fun mockDroidKaigiClient(sessions: List<Session>) = mock<DroidKaigiClient> {
        on { getSessions(any()) } doReturn Single.just(sessions)
    }
}
