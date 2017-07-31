package io.github.droidkaigi.confsched2017.view.customview

import android.animation.Animator
import android.content.Context
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.Rect
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

import io.github.droidkaigi.confsched2017.R


@BindingMethods(BindingMethod(type = OverScrollLayout::class, attribute = "onOverScroll", method = "setOverScrollListener"))
class OverScrollLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): CoordinatorLayout(context, attrs, defStyleAttr) {

    private var overScrollThreshold: Int = 0

    private val originalRect = Rect()

    private var overScrollListener: OnOverScrollListener? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        originalRect.set(left, top, right, bottom)
        overScrollThreshold = (originalRect.height() * OVER_SCROLL_THRESHOLD_RATIO).toInt()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (y == originalRect.top.toFloat()) {
            super.onNestedPreScroll(target, dx, dy, consumed)
        } else {
            val scale = Math.max(MINIMUM_OVER_SCROLL_SCALE,
                    1 - Math.abs(y) / overScrollThreshold * (1 - MINIMUM_OVER_SCROLL_SCALE))
            scaleX = scale
            scaleY = scale
            translationY((-dy).toFloat())
            consumed[1] = dy
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        var appBarLayout: AppBarLayout? = null
        var scrollTop = false
        var scrollEnd = false
        for (i in 0..childCount - 1) {
            val view = getChildAt(i)
            if (view is AppBarLayout) {
                appBarLayout = view
                continue
            }
            if (view is NestedScrollView) {
                scrollTop = !view.canScrollVertically(-1)
                scrollEnd = !view.canScrollVertically(1)
            }
        }

        if (appBarLayout == null
                || scrollTop && dyUnconsumed < 0 && isAppBarExpanded(appBarLayout)
                || scrollEnd && dyUnconsumed > 0 && isAppBarCollapsed(appBarLayout)) {
            translationY((-dyUnconsumed).toFloat())
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return y != originalRect.top.toFloat() || super.onNestedPreFling(target, velocityX, velocityY)
    }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
        if (Math.abs(y) > overScrollThreshold) {
            val yTranslation: Float = (if (originalRect.top + y > 0) originalRect.height() else -originalRect.height()).toFloat()
            animate()
                    .setDuration(context.resources.getInteger(R.integer.activity_transition_mills).toLong())
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .alpha(0f)
                    .translationY(yTranslation)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            overScrollListener?.onOverScroll()
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })
            return
        }
        if (y != originalRect.top.toFloat()) {
            animate()
                    .setDuration(RESTORE_ANIM_DURATION.toLong())
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .translationY(originalRect.top.toFloat())
                    .scaleX(1f)
                    .scaleY(1f)
        }
    }

    private fun translationY(dy: Float) {
        y += dy * 0.5f
    }

    private fun isAppBarExpanded(appBarLayout: AppBarLayout): Boolean {
        return appBarLayout.top == 0
    }

    private fun isAppBarCollapsed(appBarLayout: AppBarLayout): Boolean {
        return appBarLayout.y == (-appBarLayout.totalScrollRange).toFloat()
    }

    fun setOverScrollListener(listener: OnOverScrollListener?) {
        overScrollListener = listener
    }

    interface OnOverScrollListener {
        fun onOverScroll()
    }

    companion object {
        const val OVER_SCROLL_THRESHOLD_RATIO = 0.20f

        const val RESTORE_ANIM_DURATION = 100

        const val MINIMUM_OVER_SCROLL_SCALE = 0.99f
    }
}
