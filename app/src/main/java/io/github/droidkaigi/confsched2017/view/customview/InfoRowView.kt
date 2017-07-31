package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ViewInfoRowBinding
import io.github.droidkaigi.confsched2017.view.helper.DataBindingHelper

class InfoRowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding = ViewInfoRowBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        if (!isInEditMode) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.InfoRow)

            val title = a.getString(R.styleable.InfoRow_infoTitle)
            val description = a.getString(R.styleable.InfoRow_infoDescription)

            binding.txtInfoTitle.text = title
            binding.txtInfoDescription.text = description
            DataBindingHelper.setTextLinkify(binding.txtInfoDescription, true)

            a.recycle()
        }
    }

    fun setTitle(title: String) {
        binding.txtInfoTitle.text = title
    }

    fun setDescription(description: String) {
        binding.txtInfoDescription.text = description
    }

    companion object {
        private val TAG = InfoRowView::class.java.simpleName
    }

}
