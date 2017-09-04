package io.github.droidkaigi.confsched2017.view.helper

import android.app.Activity
import android.net.Uri
import android.support.annotation.StringRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.webkit.URLUtil

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.di.scope.ActivityScope
import io.github.droidkaigi.confsched2017.model.Session
import io.github.droidkaigi.confsched2017.view.activity.ContributorsActivity
import io.github.droidkaigi.confsched2017.view.activity.LicensesActivity
import io.github.droidkaigi.confsched2017.view.activity.SessionDetailActivity
import io.github.droidkaigi.confsched2017.view.activity.SessionFeedbackActivity
import io.github.droidkaigi.confsched2017.view.activity.SponsorsActivity
import kotlin.reflect.KClass

/**
 * Created by shihochan on 2017/02/15.
 */

@ActivityScope
class Navigator @Inject constructor(private val activity: AppCompatActivity) {

    fun navigateToSessionDetail(session: Session, parentClass: KClass<out Activity>?) {
        activity.startActivity(SessionDetailActivity.createIntent(activity, session.id, parentClass))
    }

    fun navigateToFeedbackPage(session: Session) {
        activity.startActivity(SessionFeedbackActivity.createIntent(activity, session.id))
    }

    fun navigateToSponsorsPage() {
        activity.startActivity(SponsorsActivity.createIntent(activity))
    }

    fun navigateToContributorsPage() {
        activity.startActivity(ContributorsActivity.createIntent(activity))
    }

    fun navigateToLicensePage() {
        activity.startActivity(LicensesActivity.createIntent(activity))
    }

    fun navigateToWebPage(url: String?) {
        if (TextUtils.isEmpty(url) || !URLUtil.isNetworkUrl(url)) {
            return
        }

        val intent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(activity, R.color.theme))
                .build()

        intent.launchUrl(activity, Uri.parse(url))
    }

    fun showConfirmDialog(@StringRes titleResId: Int, @StringRes messageResId: Int, listener: ConfirmDialogListener) {
        AlertDialog.Builder(activity, R.style.DialogTheme)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(android.R.string.ok) { _, _ -> listener.onClickPositiveButton() }
                .setNegativeButton(android.R.string.cancel) { _, _ -> listener.onClickNegativeButton() }
                .show()
    }

    interface ConfirmDialogListener {
        fun onClickPositiveButton()

        fun onClickNegativeButton()
    }

}
