package io.github.droidkaigi.confsched2017.repository.contributors

import com.sys1yagi.kmockito.invoked
import com.sys1yagi.kmockito.mock
import com.sys1yagi.kmockito.verify
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
        localDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        remoteDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        repository.findAll().test().assertOf { check ->
            check.assertNoErrors()
            check.assertValue(listOf())
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateLocalWhenRemoteReturns() {
        localDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        remoteDataSource.findAll().invoked.thenReturn(Single.just(CONTRIBUTORS))
        repository.findAll().test().assertOf { check ->
            check.assertNoErrors()
            localDataSource.verify(times(1)).updateAllAsync(CONTRIBUTORS)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnCache() {
        localDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        remoteDataSource.findAll().invoked.thenReturn(Single.just(CONTRIBUTORS))

        repository.findAll().flatMap { repository.findAll() }.test().run {
            assertNoErrors()
            localDataSource.verify(never()).findAll()
            remoteDataSource.verify(times(1)).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun findAllFromLocalDataSourceWhenNotDirty() {
        localDataSource.findAll().invoked.thenReturn(Single.just(CONTRIBUTORS))
        remoteDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        repository.dirty = false

        repository.findAll().test().run {
            assertNoErrors()
            localDataSource.verify(times(1)).findAll()
            remoteDataSource.verify(never()).findAll()
        }
    }

    @Test
    @Throws(Exception::class)
    fun findAllFromRemoteDataSourceWhenLocalDataSourceReturnsEmptyResult() {
        localDataSource.findAll().invoked.thenReturn(Single.just(listOf()))
        remoteDataSource.findAll().invoked.thenReturn(Single.just(CONTRIBUTORS))
        repository.dirty = false

        repository.findAll().test().run {
            assertNoErrors()
            localDataSource.verify(times(1)).findAll()
            remoteDataSource.verify(times(1)).findAll()
        }
    }

}
