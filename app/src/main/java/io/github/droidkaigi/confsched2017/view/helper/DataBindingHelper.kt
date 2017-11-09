package io.github.droidkaigi.confsched2017.view.helper

import com.squareup.picasso.Picasso

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout

import org.lucasr.twowayview.widget.SpannableGridLayoutManager

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.text.util.Linkify
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.model.Topic
import io.github.droidkaigi.confsched2017.view.customview.InfoRowView
import io.github.droidkaigi.confsched2017.view.customview.SettingSwitchRowView
import io.github.droidkaigi.confsched2017.view.customview.transformation.CropCircleTransformation

object DataBindingHelper {

    //--------------------------------------------------------------
    // Common
    //--------------------------------------------------------------
    @BindingAdapter("photoImageUrl")
    @JvmStatic
    fun setPhotoImageUrl(imageView: ImageView, imageUrl: String?) {
        setImageUrl(imageView, imageUrl, R.color.grey200)
    }

    private fun setImageUrl(imageView: ImageView, imageUrl: String?, @DrawableRes placeholderResId: Int) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setImageDrawable(
                    ContextCompat.getDrawable(imageView.context, placeholderResId))
        } else {
            Picasso.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(placeholderResId)
                    .error(placeholderResId)
                    .into(imageView)
        }
    }

    @BindingAdapter("speakerImageUrl", "speakerImageSize")
    @JvmStatic
    fun setSpeakerImageUrlWithSize(imageView: ImageView, imageUrl: String?, sizeInDimen: Float) {
        setImageUrlWithSize(imageView, imageUrl, sizeInDimen, R.drawable.ic_speaker_placeholder)
    }

    private fun setImageUrlWithSize(imageView: ImageView, imageUrl: String?, sizeInDimen: Float, placeholderResId: Int) {
        if (TextUtils.isEmpty(imageUrl)) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, placeholderResId))
        } else {
            val size = Math.round(sizeInDimen)
            imageView.background = ContextCompat.getDrawable(imageView.context, R.drawable.circle_border_grey200)
            Picasso.with(imageView.context)
                    .load(imageUrl)
                    .resize(size, size)
                    .centerInside()
                    .placeholder(placeholderResId)
                    .error(placeholderResId)
                    .transform(CropCircleTransformation())
                    .into(imageView)
        }
    }

    @BindingAdapter("textLinkify")
    @JvmStatic
    fun setTextLinkify(textView: TextView, isLinkify: Boolean) {
        if (isLinkify) {
            Linkify.addLinks(textView, Linkify.ALL)
        }
    }

    @BindingAdapter("webViewUrl")
    @JvmStatic
    fun loadUrl(webView: WebView, url: String) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        webView.loadUrl(url)
    }

    @BindingAdapter("webViewClient")
    @JvmStatic
    fun setWebViewClient(webView: WebView, client: WebViewClient) {
        webView.webViewClient = client
    }

    //--------------------------------------------------------------
    // Settings
    //--------------------------------------------------------------
    @BindingAdapter("settingEnabled")
    @JvmStatic
    fun setSettingEnabled(view: SettingSwitchRowView, enabled: Boolean) {
        view.isEnabled = enabled
    }

    @BindingAdapter("settingDefaultValue")
    @JvmStatic
    fun setSettingDefaultValue(view: SettingSwitchRowView, defaultValue: Boolean) {
        view.setDefault(defaultValue)
    }

    @BindingAdapter("settingOnCheckedChanged")
    @JvmStatic
    fun setSettingOnCheckedChanged(view: SettingSwitchRowView, listener: CompoundButton.OnCheckedChangeListener) {
        view.onCheckedChangeListener = listener
    }


    //--------------------------------------------------------------
    // Sessions
    //--------------------------------------------------------------
    @BindingAdapter("twowayview_rowSpan")
    @JvmStatic
    fun setTwowayViewRowSpan(view: View, rowSpan: Int) {
        val lp = view.layoutParams as SpannableGridLayoutManager.LayoutParams
        lp.rowSpan = rowSpan
        view.layoutParams = lp
    }

    @BindingAdapter("twowayview_colSpan")
    @JvmStatic
    fun setTwowayViewColSpan(view: View, colSpan: Int) {
        val lp = view.layoutParams as SpannableGridLayoutManager.LayoutParams
        lp.colSpan = colSpan
        view.layoutParams = lp
    }

    @BindingAdapter("sessionCellBackground")
    @JvmStatic
    fun setSessionCellBackground(view: View, @DrawableRes backgroundResId: Int) {
        view.setBackgroundResource(backgroundResId)
    }

    @BindingAdapter("sessionTopicColor")
    @JvmStatic
    fun setSessionTopicColor(view: View, @ColorRes colorResId: Int) {
        if (colorResId > 0) {
            view.setBackgroundColor(ResourcesCompat.getColor(view.resources, colorResId, null))
        }
    }

    @BindingAdapter("topicVividColor")
    @JvmStatic
    fun setTopicVividColor(view: CollapsingToolbarLayout, @ColorRes colorResId: Int) {
        view.setContentScrimColor(ContextCompat.getColor(view.context, colorResId))
    }

    @BindingAdapter("topicVividColor")
    @JvmStatic
    fun setTopicVividColor(view: TextView, @ColorRes colorResId: Int) {
        view.setTextColor(ContextCompat.getColor(view.context, colorResId))
    }

    @BindingAdapter("topicVividColor")
    @JvmStatic
    fun setTopicVividColor(view: FloatingActionButton, @ColorRes colorResId: Int) {
        view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, colorResId))
    }

    @BindingAdapter("coverFadeBackground")
    @JvmStatic
    fun setCoverFadeBackground(view: View, @ColorRes colorResId: Int) {
        view.setBackgroundResource(colorResId)
    }

    @BindingAdapter("sessionStatus")
    @JvmStatic
    fun setSessionStatus(view: FloatingActionButton, isMySession: Boolean) {
        if (isMySession) {
            view.setImageResource(R.drawable.avd_check_to_add_24dp)
            view.isSelected = true
        } else {
            view.setImageResource(R.drawable.avd_add_to_check_24dp)
            view.isSelected = false
        }
    }

    @BindingAdapter("topic")
    @JvmStatic
    fun setTopic(textView: TextView, topic: Topic?) {
        topic?.apply {
            textView.setBackgroundResource(R.drawable.tag_language)
            textView.text = name
        } ?: run {
            textView.visibility = View.INVISIBLE
        }
    }


    //--------------------------------------------------------------
    // Information
    //--------------------------------------------------------------
    @BindingAdapter("infoRowDescription")
    @JvmStatic
    fun setInfoRowDescription(view: InfoRowView, description: String) {
        view.setDescription(description)
    }


    //--------------------------------------------------------------
    // SearchResult
    //--------------------------------------------------------------
    @BindingAdapter("searchResultIcon", "mySession")
    @JvmStatic
    fun setSessionIcon(textView: TextView, @DrawableRes iconResId: Int, isMySession: Boolean) {
        val context = textView.context
        val icon = ContextCompat.getDrawable(context, iconResId)
        val checkMark = ContextCompat.getDrawable(context, R.drawable.ic_check_circle_24_vector)
        val size = context.resources.getDimensionPixelSize(R.dimen.text_drawable_12dp)
        checkMark?.setBounds(0, 0, size, size)
        icon?.setBounds(0, 0, size, size)
        textView.setCompoundDrawables(icon, null, if (isMySession) checkMark else null, null)
    }
}
