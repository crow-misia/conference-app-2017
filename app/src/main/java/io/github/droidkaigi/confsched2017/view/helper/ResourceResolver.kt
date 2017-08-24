package io.github.droidkaigi.confsched2017.view.helper

import android.content.Context
import android.support.annotation.StringRes

import java.io.IOException

import javax.inject.Inject
import javax.inject.Singleton

import okio.Okio
import timber.log.Timber

@Singleton
open class ResourceResolver @Inject constructor(private val context: Context) {

    open fun getString(@StringRes resId: Int): String = context.getString(resId)

    open fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)

    open fun loadJSONFromAsset(jsonFileName: String): String? {
        try {
            context.assets.open("json/" + jsonFileName).use {
                Okio.buffer(Okio.source(it)).use {
                    return it.readUtf8()
                }
            }
        } catch (e: IOException) {
            Timber.tag(TAG).e(e, "assets/json/%s: read failed", jsonFileName)
        }
        return null
    }

    companion object {
        private val TAG = ResourceResolver::class.java.simpleName
    }
}
