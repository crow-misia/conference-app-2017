package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import org.lucasr.twowayview.widget.TwoWayView

class TouchlessTwoWayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : TwoWayView(context, attrs, defStyle) {

    override fun dispatchTouchEvent(ev: MotionEvent) = false

    fun forceToDispatchTouchEvent(ev: MotionEvent) = super.dispatchTouchEvent(ev)
}
