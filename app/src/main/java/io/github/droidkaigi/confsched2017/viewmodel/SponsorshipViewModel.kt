package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableList

import com.annimon.stream.Stream
import io.github.droidkaigi.confsched2017.model.Sponsor

import io.github.droidkaigi.confsched2017.model.Sponsorship
import io.github.droidkaigi.confsched2017.view.helper.Navigator

class SponsorshipViewModel constructor(private val navigator: Navigator, val sponsorship: Sponsorship) : BaseObservable(), ViewModel {

    val category: String = sponsorship.category

    val sponsorViewModels = ObservableArrayList<SponsorViewModel>().apply {
        addAll(sponsorship.sponsors.map { SponsorViewModel(navigator, it) })
    }

    override fun destroy() {
        // No-op
    }
}
