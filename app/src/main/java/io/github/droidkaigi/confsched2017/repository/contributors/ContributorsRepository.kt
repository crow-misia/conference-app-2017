package io.github.droidkaigi.confsched2017.repository.contributors

import java.util.ArrayList
import java.util.LinkedHashMap

import javax.inject.Inject
import javax.inject.Singleton

import io.github.droidkaigi.confsched2017.model.Contributor
import io.reactivex.Single

@Singleton
class ContributorsRepository @Inject internal constructor(private val localDataSource: ContributorsLocalDataSource, private val remoteDataSource: ContributorsRemoteDataSource) {

    private val cachedContributors = LinkedHashMap<String, Contributor>()

    var dirty: Boolean = true

    fun findAll(): Single<List<Contributor>> {
        if (!cachedContributors.isEmpty() && !dirty) {
            return Single.create { emitter -> emitter.onSuccess(ArrayList(cachedContributors.values)) }
        }

        if (dirty) {
            return findAllFromRemote()
        }
        return findAllFromLocal()
    }

    private fun findAllFromRemote(): Single<List<Contributor>> =
        remoteDataSource.findAll()
                .doOnSuccess { contributors ->
                    refreshCache(contributors)
                    localDataSource.updateAllAsync(contributors)
                }

    private fun findAllFromLocal(): Single<List<Contributor>> =
        localDataSource.findAll().flatMap<List<Contributor>> { contributors ->
            if (contributors.isEmpty()) {
                return@flatMap findAllFromRemote()
            } else {
                refreshCache(contributors)
                return@flatMap Single.create { emitter -> emitter.onSuccess(contributors) }
            }
        }

    private fun refreshCache(contributors: List<Contributor>) {
        cachedContributors.clear()
        for (contributor in contributors) {
            cachedContributors.put(contributor.name, contributor)
        }
        dirty = false
    }
}
