package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.support.annotation.StringRes
import android.view.View

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.BR
import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.di.scope.FragmentScope
import io.github.droidkaigi.confsched2017.repository.contributors.ContributorsRepository
import io.github.droidkaigi.confsched2017.view.helper.ResourceResolver
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@FragmentScope
class ContributorsViewModel @Inject internal constructor(
        private val resourceResolver: ResourceResolver,
        private val navigator: Navigator,
        private val toolbarViewModel: ToolbarViewModel,
        private val contributorsRepository: ContributorsRepository,
        private val compositeDisposable: CompositeDisposable) : BaseObservable(), ViewModel {

    val contributorViewModels = ObservableArrayList<ContributorViewModel>()

    @get:Bindable
    var loadingVisibility: Int = 0
        private set(visibility) {
            field = visibility
            notifyPropertyChanged(BR.loadingVisibility)
        }

    @get:Bindable
    var refreshing: Boolean = false
        private set(refreshing) {
            field = refreshing
            notifyPropertyChanged(BR.refreshing)
        }

    var callback: Callback? = null

    fun start() {
        loadContributors(false)
    }

    override fun destroy() {
        compositeDisposable.clear()
        this.callback = null
    }

    fun onSwipeRefresh() {
        loadContributors(true)
    }

    fun retry() {
        loadContributors(false)
    }

    fun onClickRepositoryMenu() {
        navigator.navigateToWebPage("https://github.com/DroidKaigi/conference-app-2017")
    }

    private fun loadContributors(refresh: Boolean) {
        if (refresh) {
            contributorsRepository.dirty = true
        } else {
            loadingVisibility = View.VISIBLE
        }

        contributorsRepository.findAll()
                .map { it.map { contributor -> ContributorViewModel(navigator, contributor) }.toList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { this.renderContributors(it) },
                        onError = {
                            loadingVisibility = View.GONE
                            callback?.showError(R.string.contributors_load_failed)
                            Timber.tag(TAG).e(it, "Failed to show contributors.")
                        }
                ).addTo(compositeDisposable)
    }

    private fun renderContributors(contributorViewModels: List<ContributorViewModel>) {
        this.contributorViewModels.clear()
        this.contributorViewModels.addAll(contributorViewModels)

        val title = resourceResolver.getString(R.string.contributors) + " " + resourceResolver.getString(R.string.contributors_people, contributorViewModels.size)
        toolbarViewModel.toolbarTitle = title

        loadingVisibility = View.GONE
        refreshing = false
    }

    interface Callback {
        fun showError(@StringRes textRes: Int)
    }

    companion object {
        private val TAG = ContributorsViewModel::class.java.simpleName
    }
}
