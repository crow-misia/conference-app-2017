package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.databinding.ViewOverlayDebugBinding
import io.github.droidkaigi.confsched2017.di.OverlayViewModule
import io.github.droidkaigi.confsched2017.log.LogEmitter
import io.github.droidkaigi.confsched2017.service.DebugOverlayService
import io.reactivex.disposables.CompositeDisposable

/**
 * This view renders logcat stream.
 * @author KeithYokoma
 */
class DebugOverlayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    @Inject
    lateinit var disposables: CompositeDisposable
    @Inject
    lateinit var emitter: LogEmitter

    private val binding: ViewOverlayDebugBinding = ViewOverlayDebugBinding.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, this, true)

    init {
        unwrapContext(context).component.plus(OverlayViewModule()).inject(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposables.add(emitter.listen().subscribe { log ->
            // prepend a log
            binding.debugMessages.text = log.message + "\n" + binding.debugMessages.text
        })
    }

    override fun onDetachedFromWindow() {
        disposables.clear()
        super.onDetachedFromWindow()
    }

    // Context in the ctor is ContextThemeWrapper and it wraps OverlayViewContext, which wraps DebugOverlayService
    private fun unwrapContext(context: Context): DebugOverlayService {
        val wrapper = context as ContextWrapper
        return wrapper.baseContext as DebugOverlayService
    }
}
