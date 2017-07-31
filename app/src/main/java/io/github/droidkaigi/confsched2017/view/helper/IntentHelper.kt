package io.github.droidkaigi.confsched2017.view.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.webkit.URLUtil

import timber.log.Timber

object IntentHelper {

    /**
     * Builds an intent of type Intent.ACTION_VIEW from the passed htmlUrl.
     * But, When intent.resolveActivity(context.getPackageManager()) == null is true, it return null.
     */
    fun buildActionViewIntent(context: Context, htmlUrl: String): Intent? {
        if (TextUtils.isEmpty(htmlUrl)) {
            Timber.i("buildActionViewIntent: url is null")
            return null
        }

        Timber.i("buildActionViewIntent: url: %s", htmlUrl)

        if (!URLUtil.isNetworkUrl(htmlUrl)) {
            return null
        }

        val uri = Uri.parse(htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        return intent.resolveActivity(context.packageManager)?.let { intent }
    }
}
