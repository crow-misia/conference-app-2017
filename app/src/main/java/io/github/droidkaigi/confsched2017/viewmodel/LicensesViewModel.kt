package io.github.droidkaigi.confsched2017.viewmodel

import android.annotation.TargetApi
import android.databinding.BaseObservable
import android.os.Build
import android.text.TextUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.view.helper.Navigator

class LicensesViewModel @Inject
internal constructor(private val navigator: Navigator) : BaseObservable(), ViewModel {

    val licenseFilePath = "file:///android_asset/licenses.html"

    val webViewClient: WebViewClient

    init {
        webViewClient = LicensesWebViewClient()
    }

    override fun destroy() {
        // Nothing to do
    }

    private fun shouldOverrideUrlLoading(url: String): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        if (url == licenseFilePath) {
            return false
        } else {
            navigator.navigateToWebPage(url)
            return true
        }
    }

    private inner class LicensesWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return this@LicensesViewModel.shouldOverrideUrlLoading(url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString())
        }
    }
}
