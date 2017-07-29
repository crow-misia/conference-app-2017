package io.github.droidkaigi.confsched2017.repository.contributors

import com.github.gfx.android.orma.annotation.OnConflict

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.model.Contributor
import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ContributorsLocalDataSource @Inject internal constructor(private val orma: OrmaDatabase) {

    fun findAll(): Single<List<Contributor>> =
        orma.selectFromContributor()
                .executeAsObservable()
                .toList()
                .subscribeOn(Schedulers.io())

    private fun updateAllSync(contributors: List<Contributor>) =
        orma.prepareInsertIntoContributor(OnConflict.REPLACE).executeAll(contributors)

    internal fun updateAllAsync(contributors: List<Contributor>): Disposable =
        orma.transactionAsCompletable { updateAllSync(contributors) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
}
