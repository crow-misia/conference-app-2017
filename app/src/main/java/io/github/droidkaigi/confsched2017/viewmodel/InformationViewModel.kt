package io.github.droidkaigi.confsched2017.viewmodel

import android.view.View

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.BuildConfig
import io.github.droidkaigi.confsched2017.view.helper.Navigator

class InformationViewModel @Inject
constructor(private val navigator: Navigator) : ViewModel {

    private val versionName = "V" + BuildConfig.VERSION_NAME

    fun getVersionName() = versionName + " " + BuildConfig.GIT_SHA

    fun onClickSponsors(view: View) = navigator.navigateToSponsorsPage()

    fun onClickQuestionnaire(view: View) = navigator.navigateToWebPage("https://docs.google.com/forms/d/1SNBvJernnyBwglNentXxpdSUkWI9U6umWdDs4Na8OIU/viewform?edit_requested=true")

    fun onClickContributors(view: View) = navigator.navigateToContributorsPage()

    fun onClickHelpTranslate(view: View) = navigator.navigateToWebPage("https://droidkaigi2017.oneskyapp.com/collaboration")

    fun onClickLicense(view: View) = navigator.navigateToLicensePage()

    fun onClickDevInfo(view: View) {
        // TODO
    }

    fun onClickTwitter(view: View) = navigator.navigateToWebPage("https://twitter.com/DroidKaigi")

    fun onClickFacebook(view: View) = navigator.navigateToWebPage("https://www.facebook.com/DroidKaigi/")

    fun onClickGitHub(view: View) = navigator.navigateToWebPage("https://github.com/DroidKaigi/conference-app-2017/")

    fun onClickDroidKaigiWeb(view: View) = navigator.navigateToWebPage("https://droidkaigi.github.io/2017/")

    fun onClickYouTube(view: View) = navigator.navigateToWebPage("https://www.youtube.com/droidkaigi")

    override fun destroy() {
        // Nothing to do
    }
}
