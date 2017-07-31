package io.github.droidkaigi.confsched2017.view.helper

import android.graphics.drawable.Animatable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.ImageView

object AnimationHelper {
    @JvmStatic
    fun startVDAnimation(imageView: ImageView,
                         @DrawableRes inactiveResId: Int,
                         @DrawableRes activeResId: Int,
                         duration: Int) {
        val drawableResId = if (imageView.isSelected) activeResId else inactiveResId
        val drawable = ContextCompat.getDrawable(imageView.context, drawableResId)
        imageView.setImageDrawable(drawable)

        if (drawable is Animatable) {
            if (drawable.isRunning) {
                drawable.stop()
            }

            drawable.start()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.isSelected = !imageView.isSelected
            } else {
                imageView.postDelayed({
                    imageView.isSelected = !imageView.isSelected
                    val nextDrawableResId = if (imageView.isSelected) activeResId else inactiveResId
                    val nextDrawable = ContextCompat.getDrawable(imageView.context, nextDrawableResId)
                    imageView.setImageDrawable(nextDrawable)
                }, duration.toLong())
            }
        }
    }

}
