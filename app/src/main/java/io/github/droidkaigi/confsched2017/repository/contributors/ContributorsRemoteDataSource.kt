package io.github.droidkaigi.confsched2017.repository.contributors

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.api.DroidKaigiClient
import io.github.droidkaigi.confsched2017.model.Contributor
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ContributorsRemoteDataSource @Inject internal constructor(private val client: DroidKaigiClient) {

    internal fun findAll(): Single<List<Contributor>> = client.contributors().subscribeOn(Schedulers.io())
}
