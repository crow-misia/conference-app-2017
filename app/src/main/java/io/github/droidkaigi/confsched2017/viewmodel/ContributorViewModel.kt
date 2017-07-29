package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.view.View

import io.github.droidkaigi.confsched2017.model.Contributor
import io.github.droidkaigi.confsched2017.view.helper.Navigator

class ContributorViewModel(private val navigator: Navigator, val contributor: Contributor) : BaseObservable(), ViewModel {

    val name = contributor.name

    val avatarUrl = contributor.avatarUrl

    val htmlUrl = contributor.htmlUrl

    val contributions = contributor.contributions

    override fun destroy() {
        // Nothing to do
    }

    fun onClickContributor(view: View?) = navigator.navigateToWebPage(htmlUrl)
}
