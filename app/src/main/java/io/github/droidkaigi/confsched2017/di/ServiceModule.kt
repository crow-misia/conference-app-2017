package io.github.droidkaigi.confsched2017.di

import android.app.Service
import android.view.WindowManager

import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2017.service.helper.OverlayViewManager

/**
 * Created by KeishinYokomaku on 2017/02/12.
 */
@Module
class ServiceModule(private val service: Service) {

    @Provides
    fun service() = service

    @Provides
    fun overlayViewManager(windowManager: WindowManager) = OverlayViewManager(service, windowManager)
}
