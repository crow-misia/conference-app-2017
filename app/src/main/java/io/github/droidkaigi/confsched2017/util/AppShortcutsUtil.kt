package io.github.droidkaigi.confsched2017.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.support.annotation.RequiresApi

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.view.activity.MainActivity
import io.github.droidkaigi.confsched2017.view.activity.SearchActivity

object AppShortcutsUtil {

    private val APP_SHORTCUTS_SEARCH_ID = "search"

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    fun addShortcuts(context: Context) {
        val shortcutManager = context.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager?

        val intents = arrayOf(context.applicationContext.intentFor<MainActivity>().setAction(Intent.ACTION_DEFAULT), context.applicationContext.intentFor<SearchActivity>().setAction(Intent.ACTION_DEFAULT))

        val shortcutInfo = ShortcutInfo.Builder(context, APP_SHORTCUTS_SEARCH_ID)
                .setShortLabel(context.getString(R.string.shortcut_search_title))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_search_24_vector))
                .setIntents(intents)
                .build()

        shortcutManager?.addDynamicShortcuts(listOf(shortcutInfo))
    }
}
