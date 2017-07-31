package io.github.droidkaigi.confsched2017.repository.contributors

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2017.util.DummyCreator
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.times

class ContributorsRepositoryTest {

    private companion object {
        val CONTRIBUTORS = listOf(
                DummyCreator.newContributor(0).apply {
                    name = "Alice"
                },
                DummyCreator.newContributor(0).apply {
                    name = "Bob"
                },
                DummyCreator.newContributor(0).apply {
                    name = "Charlie"
                }
        )
    }

    private val localDataSource = mock<ContributorsLocalDataSource>()
    private val remoteDataSource = mock<ContributorsRemoteDataSource>()

    private val repository = ContributorsRepository(localDataSource, remoteDataSource)

    @Test
    @Throws(Exception::class)
    fun findAllFromEmptyRepository() {
        whenever(localDataSource.findAll()) doReturn Single.just(listOf())
        whenever(remoteDataSource.findAll()) doReturn Single.just(listOf())
        repository.findAll().test().assertOf { check ->
            check.assertNoErrors()
            check.assertValue(listOf())
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateLocalWhenRemoteReturns() {
        whenever(localDataSource.findAll()) doReturn Single.just(listOf())
        whenever(remoteDataSource.findAll()) doReturn Single.just(CONTRIBUTORS)
        repository.findAll().test().assertOf { check ->
            check.assertNoErrors()
            verify(localDataSource, times(1)).updateAllAsync(CONTRIBUTORS)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnCache() {
        whenever(localDataSource.findAll()) doReturn Single.just(listOf())
        whenever(remoteDataSource.findAll()) doReturn Single.just(CONTRIBUTORS)

        repository.findAll().flatMap { repository.findAll() }.test().run {
            assertNoErrors()
            verify(localDataSource, never()).findAll()
            verify(remoteDataSource, times(1)).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun findAllFromLocalDataSourceWhenNotDirty() {
        whenever(localDataSource.findAll()) doReturn Single.just(CONTRIBUTORS)
        whenever(remoteDataSource.findAll()) doReturn Single.just(listOf())
        repository.dirty = false

        repository.findAll().test().run {
            assertNoErrors()
            verify(localDataSource, times(1)).findAll()
            verify(remoteDataSource, never()).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun findAllFromRemoteDataSourceWhenLocalDataSourceReturnsEmptyResult() {
        whenever(localDataSource.findAll()) doReturn Single.just(listOf())
        whenever(remoteDataSource.findAll()) doReturn Single.just(CONTRIBUTORS)
        repository.dirty = false

        repository.findAll().test().run {
            assertNoErrors()
            verify(localDataSource, times(1)).findAll()
            verify(remoteDataSource, times(1)).findAll()
        }
    }
}
