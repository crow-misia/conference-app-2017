package io.github.droidkaigi.confsched2017.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.github.gfx.android.orma.AccessThreadConstraint

import android.app.Application
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2017.BuildConfig
import io.github.droidkaigi.confsched2017.api.RequestInterceptor
import io.github.droidkaigi.confsched2017.api.service.DroidKaigiService
import io.github.droidkaigi.confsched2017.api.service.GithubService
import io.github.droidkaigi.confsched2017.api.service.GoogleFormService
import io.github.droidkaigi.confsched2017.model.OrmaDatabase
import io.github.droidkaigi.confsched2017.pref.DefaultPrefs
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by KeishinYokomaku on 2017/01/20.
 */
@Module
class AppModule(private val app: Application) {

    @Provides
    fun provideContext(): Context = app

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    fun provideDefaultPrefs(context: Context): DefaultPrefs {
        return DefaultPrefs.get(context)
    }

    @Singleton
    @Provides
    fun provideOrmaDatabase(context: Context): OrmaDatabase {
        return OrmaDatabase.builder(context)
                .readOnMainThread(AccessThreadConstraint.WARNING)
                .writeOnMainThread(AccessThreadConstraint.WARNING)
                .build()
    }

    @Provides
    fun provideRequestInterceptor(interceptor: RequestInterceptor): Interceptor {
        return interceptor
    }

    @Singleton
    @Provides
    fun provideDroidKaigiService(client: OkHttpClient): DroidKaigiService {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.API_ROOT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()
                .create(DroidKaigiService::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubService(client: OkHttpClient): GithubService {
        return Retrofit.Builder().client(client)
                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()
                .create(GithubService::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleFormService(client: OkHttpClient): GoogleFormService {
        return Retrofit.Builder().client(client)
                .baseUrl("https://docs.google.com/forms/d/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()
                .create(GoogleFormService::class.java)
    }

    companion object {

        internal val SHARED_PREF_NAME = "preferences"

        private fun createGson(): Gson {
            return GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create()
        }
    }
}
