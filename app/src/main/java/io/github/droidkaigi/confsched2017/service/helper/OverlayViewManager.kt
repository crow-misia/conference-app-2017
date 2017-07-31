package io.github.droidkaigi.confsched2017.service.helper

import android.content.Context
import android.graphics.PixelFormat
import android.support.v4.view.GravityCompat
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @author KeithYokoma
 */
class OverlayViewManager @Inject
constructor(private val context: Context, private val windowManager: WindowManager) {
    private val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, 0, 0,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT)
    private var themedContext: Context? = null
    private var root: View? = null

    init {
        params.gravity = GravityCompat.START or Gravity.TOP
    }

    fun create() {
        themedContext = OverlayViewContext(context)
        val inflater = LayoutInflater.from(themedContext)
        root = inflater.inflate(R.layout.view_root_overlay, null, false)
        windowManager.addView(root, params)
    }

    fun changeConfiguration() {
        root?.let { windowManager.updateViewLayout(it, params) }
    }

    fun destroy() {
        root?.let {
            windowManager.removeViewImmediate(it)
            themedContext = null
        }
    }

    /* package */ internal class OverlayViewContext(base: Context) : ContextThemeWrapper(base, R.style.AppTheme) {
        private var inflater: LayoutInflater? = null

        override fun attachBaseContext(base: Context) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
        }

        override fun getSystemService(name: String): Any? {
            if (Context.LAYOUT_INFLATER_SERVICE == name) {
                inflater = inflater ?: LayoutInflater.from(baseContext).cloneInContext(this)
                return inflater
            }
            return super.getSystemService(name)
        }
    }
}
