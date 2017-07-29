package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.view.View

import io.github.droidkaigi.confsched2017.model.Sponsor
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import javax.inject.Inject

class SponsorViewModel @Inject constructor(private val navigator: Navigator, val sponsor: Sponsor) : BaseObservable(), ViewModel {

    override fun destroy() {
        // Nothing to do
    }

    fun onClickSponsor(view: View?) = navigator.navigateToWebPage(sponsor.url)
}
