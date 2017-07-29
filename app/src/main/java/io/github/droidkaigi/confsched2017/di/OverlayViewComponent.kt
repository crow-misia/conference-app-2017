package io.github.droidkaigi.confsched2017.di

import dagger.Subcomponent
import io.github.droidkaigi.confsched2017.di.scope.OverlayViewScope
import io.github.droidkaigi.confsched2017.view.customview.DebugOverlayView

/**
 * @author KeithYokoma
 */
@OverlayViewScope
@Subcomponent(modules = arrayOf(OverlayViewModule::class))
interface OverlayViewComponent {
    fun inject(view: DebugOverlayView)
}
