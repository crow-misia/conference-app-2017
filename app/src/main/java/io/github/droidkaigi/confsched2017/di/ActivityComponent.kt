package io.github.droidkaigi.confsched2017.di


import dagger.Subcomponent
import io.github.droidkaigi.confsched2017.di.scope.ActivityScope
import io.github.droidkaigi.confsched2017.view.activity.*

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: BaseActivity)

    fun inject(activity: SplashActivity)

    fun inject(activity: ContributorsActivity)

    operator fun plus(module: FragmentModule): FragmentComponent
}
