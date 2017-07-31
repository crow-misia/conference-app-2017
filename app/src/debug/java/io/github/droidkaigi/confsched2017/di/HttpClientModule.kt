package io.github.droidkaigi.confsched2017.di

import com.facebook.stetho.okhttp3.StethoInterceptor

import android.content.Context

import java.io.File

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
class HttpClientModule {

    @Singleton
    @Provides
    fun provideHttpClient(context: Context, interceptor: Interceptor): OkHttpClient {
        val cacheDir = File(context.cacheDir, CACHE_FILE_NAME)
        val cache = Cache(cacheDir, MAX_CACHE_SIZE)

        return OkHttpClient.Builder().cache(cache).addInterceptor(interceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }

    companion object {
        internal const val CACHE_FILE_NAME = "okhttp.cache"

        internal const val MAX_CACHE_SIZE = (4 * 1024 * 1024).toLong()
    }
}
