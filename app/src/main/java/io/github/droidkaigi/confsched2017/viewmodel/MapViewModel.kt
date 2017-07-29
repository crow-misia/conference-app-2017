package io.github.droidkaigi.confsched2017.viewmodel

import android.databinding.BaseObservable

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.view.helper.Navigator
import io.github.droidkaigi.confsched2017.view.helper.ResourceResolver

class MapViewModel @Inject internal constructor(private val navigator: Navigator, private val resourceResolver: ResourceResolver) : BaseObservable(), ViewModel {

    fun onClickRouteMenu() = navigator.navigateToWebPage(resourceResolver.getString(R.string.map_route_guide_url))

    override fun destroy() {
        // Nothing to do
    }
}
