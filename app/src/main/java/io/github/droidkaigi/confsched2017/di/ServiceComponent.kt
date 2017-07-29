package io.github.droidkaigi.confsched2017.di

import dagger.Subcomponent
import io.github.droidkaigi.confsched2017.di.scope.ServiceScope
import io.github.droidkaigi.confsched2017.service.DebugOverlayService

/**
 * Created by KeishinYokomaku on 2017/02/12.
 */
@ServiceScope
@Subcomponent(modules = arrayOf(ServiceModule::class))
interface ServiceComponent {

    fun inject(service: DebugOverlayService)

    operator fun plus(module: OverlayViewModule): OverlayViewComponent
}
