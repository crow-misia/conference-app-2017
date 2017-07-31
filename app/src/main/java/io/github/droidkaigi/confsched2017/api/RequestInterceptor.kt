package io.github.droidkaigi.confsched2017.api

import android.net.ConnectivityManager

import java.io.IOException

import javax.inject.Inject
import javax.inject.Singleton

import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class RequestInterceptor @Inject constructor(private val connectivityManager: ConnectivityManager) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val r = chain.request().newBuilder()

        if (isConnected()) {
            val maxAge = 2 * 60
            r.addHeader("cache-control", "public, max-age=" + maxAge)
        } else {
            val maxStale = 30 * 24 * 60 * 60 // 30 days
            r.addHeader("cache-control", "public, only-if-cached, max-stale=" + maxStale)
        }

        return chain.proceed(r.build())
    }

    protected fun isConnected(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}
