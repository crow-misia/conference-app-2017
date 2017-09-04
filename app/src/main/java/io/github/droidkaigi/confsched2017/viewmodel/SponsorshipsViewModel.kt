package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Sponsorship
import io.github.droidkaigi.confsched2017.util.fromJson
import io.github.droidkaigi.confsched2017.view.helper.ResourceResolver
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class SponsorshipsViewModel @Inject internal constructor(
        private val resourceResolver: ResourceResolver, private val navigator: Navigator,
        private val compositeDisposable: CompositeDisposable) : BaseObservable(), ViewModel {

    val sponsorShipViewModels = ObservableArrayList<SponsorshipViewModel>()

    fun start() =
        loadSponsors()
                .map { this.convertToViewModel(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { this.renderSponsorships(it) },
                        onError = { Timber.tag(TAG).e(it, "Failed to show sponsors.") }
                ).addTo(compositeDisposable)

    override fun destroy() = compositeDisposable.clear()

    private fun loadSponsors() = Single.create<List<Sponsorship>> { emitter ->
        val json = resourceResolver.loadJSONFromAsset(resourceResolver.getString(R.string.sponsors_file))
        emitter.onSuccess(transformSponsorships(json))
    }

    /**
     * Transforms from a valid json string to a List of [Sponsorship].

     * @param json A json representing a list of sponsors.
     * *
     * @return List of [Sponsorship].
     */
    private fun transformSponsorships(json: String?): List<Sponsorship> =
        json?.let {
            return Gson().fromJson(it)
        } ?: emptyList()

    private fun convertToViewModel(sponsorships: List<Sponsorship>) =
        sponsorships
                .map { sponsorship -> SponsorshipViewModel(navigator, sponsorship) }
                .toList()

    private fun renderSponsorships(sponsorships: List<SponsorshipViewModel>) {
        sponsorShipViewModels.clear()
        sponsorShipViewModels.addAll(sponsorships)
    }

    companion object {
        private val TAG = SponsorshipsViewModel::class.java.simpleName
    }
}
