package io.github.droidkaigi.confsched2017.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by KeishinYokomaku on 2017/01/20.
 */
@Module
class AndroidModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun handler() = Handler(Looper.getMainLooper())

    @Provides
    @Singleton
    internal fun provideConnectivityManager() = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    internal fun windowManager(): WindowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
}
