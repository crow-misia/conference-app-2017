package io.github.droidkaigi.confsched2017.util

import android.view.View
import android.view.ViewTreeObserver

object ViewUtil {
    /**
     * @see ViewTreeObserver
     */
    interface OnGlobalLayoutListener {

        /**
         * This emulates [ViewTreeObserver.OnGlobalLayoutListener.onGlobalLayout]

         * @return if true this listener should be removed, otherwise false.
         * *
         * @see android.view.ViewTreeObserver.OnGlobalLayoutListener
         */
        fun onGlobalLayout(): Boolean
    }

    @JvmStatic
    fun addOneTimeOnGlobalLayoutListener(view: View, onGlobalLayoutListener: OnGlobalLayoutListener) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (onGlobalLayoutListener.onGlobalLayout()) {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }
}
