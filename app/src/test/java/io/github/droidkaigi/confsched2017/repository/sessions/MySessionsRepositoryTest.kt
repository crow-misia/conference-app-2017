package io.github.droidkaigi.confsched2017.repository.sessions

import com.nhaarman.mockito_kotlin.*
import com.taroid.knit.should
import io.github.droidkaigi.confsched2017.model.*
import io.github.droidkaigi.confsched2017.util.DummyCreator
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test

class MySessionsRepositoryTest {

    private val localDataSource = mock<MySessionsLocalDataSource>()
    private val repository = MySessionsRepository(localDataSource)

    @Test
    @Throws(Exception::class)
    fun findAll() {
        val expected = Array(10) { i -> DummyCreator.newMySession(i) }.toList()
        whenever(localDataSource.findAll()) doReturn Single.just(expected)

        repository.findAll().test().run {
            assertNoErrors()
            assertResult(expected)
            assertComplete()
            verify(localDataSource, times(1)).findAll()
        }

        // check if found sessions are cached
        repository.findAll().test().run {
            assertNoErrors()
            assertResult(expected)
            assertComplete()
            verify(localDataSource, times(1)).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun save() {
        val session = DummyCreator.newSession(1)
        whenever(localDataSource.save(session)) doReturn Completable.complete()
        repository.save(session).test().run {
            assertNoErrors()
            assertComplete()
        }

         // check if session is cached
        repository.findAll().test().run {
            assertNoErrors()
            assertResult(listOf(MySession(session = session)))
            assertComplete()
            verify(localDataSource, never()).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun delete() {
        val session1 = DummyCreator.newSession(1)
        val session2 = DummyCreator.newSession(2)

        // ready caches
        repository.save(session1)
        repository.save(session2)

        whenever(localDataSource.delete(session1)) doReturn Single.just(1)
        repository.delete(session1).test().run {
            assertNoErrors()
            assertResult(1)
            assertComplete()
        }

        // check if cached session1 is deleted
        repository.findAll().test().run {
            assertNoErrors()
            assertResult(listOf(MySession(session = session2)))
            assertComplete()
        }
    }

    @Test
    @Throws(Exception::class)
    fun isExist() {
        whenever(localDataSource.isExist(1)).thenReturn(false)
        repository.isExist(1).should be false

        whenever(localDataSource.isExist(1)).thenReturn(true)
        repository.isExist(1).should be true
    }

}
