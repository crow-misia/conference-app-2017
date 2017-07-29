package io.github.droidkaigi.confsched2017.di


import javax.inject.Singleton

import dagger.Component
import io.github.droidkaigi.confsched2017.MainApplication

@Singleton
@Component(modules = arrayOf(AppModule::class, AndroidModule::class, HttpClientModule::class))
interface AppComponent {

    fun inject(application: MainApplication)
    operator fun plus(module: ActivityModule): ActivityComponent
    operator fun plus(module: ServiceModule): ServiceComponent
}
