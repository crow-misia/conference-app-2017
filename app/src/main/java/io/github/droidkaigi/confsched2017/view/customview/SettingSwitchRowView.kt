package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.RelativeLayout

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ViewSettingSwitchRowBinding

class SettingSwitchRowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding = ViewSettingSwitchRowBinding.inflate(LayoutInflater.from(context), this, true)

    var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null

    init {
        if (!isInEditMode) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SettingSwitchRow)

            val title = a.getString(R.styleable.SettingSwitchRow_settingTitle)
            val description = a.getString(R.styleable.SettingSwitchRow_settingDescription)

            binding.settingTitle.text = title
            binding.settingDescription.text = description

            binding.root.setOnClickListener { _ -> toggle() }
            binding.settingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckedChangeListener?.onCheckedChanged(buttonView, isChecked)
            }

            a.recycle()
        }
    }

    private fun toggle() {
        val isChecked = binding.settingSwitch.isChecked
        binding.settingSwitch.isChecked = !isChecked
    }

    fun setChecked(checked: Boolean) {
        binding.settingSwitch.isChecked = checked
    }

    fun init(defaultValue: Boolean, listener: CompoundButton.OnCheckedChangeListener) {
        setDefault(defaultValue)
        onCheckedChangeListener = listener
    }

    fun setDefault(defaultValue: Boolean) {
        binding.settingSwitch.isChecked = defaultValue
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.root.isEnabled = enabled
        binding.settingSwitch.isEnabled = enabled
        if (enabled) {
            binding.settingTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.settingDescription.setTextColor(ContextCompat.getColor(context, R.color.grey600))
        } else {
            val disabledTextColor = ContextCompat.getColor(context, R.color.black_alpha_30)
            binding.settingTitle.setTextColor(disabledTextColor)
            binding.settingDescription.setTextColor(disabledTextColor)
        }
    }
}
