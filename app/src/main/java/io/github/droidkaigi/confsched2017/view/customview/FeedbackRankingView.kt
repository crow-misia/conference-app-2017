package io.github.droidkaigi.confsched2017.view.customview

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.databinding.InverseBindingMethod
import android.databinding.InverseBindingMethods
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ViewFeedbackRankingBinding

@InverseBindingMethods(InverseBindingMethod(type = FeedbackRankingView::class, attribute = "currentRanking", method = "getCurrentRanking"))
class FeedbackRankingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val maxSize: Int

    private val labelStart: String

    private val labelEnd: String

    private var currentRanking: Int = 0

    private val binding = ViewFeedbackRankingBinding.inflate(LayoutInflater.from(context), this, true)

    var listener: OnCurrentRankingChangeListener? = null

    init {
        if (isInEditMode) {
            maxSize = 0
            labelStart = ""
            labelEnd = ""
        } else {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FeedbackRankingView)
            maxSize = a.getInteger(R.styleable.FeedbackRankingView_rankingMaxSize, DEFAULT_MAX_SIZE)
            labelStart = a.getString(R.styleable.FeedbackRankingView_rankingLabelStart)
            labelEnd = a.getString(R.styleable.FeedbackRankingView_rankingLabelEnd)
            a.recycle()

            initView()
        }
    }

    private fun initView() {
        if (labelStart.isNotEmpty()) {
            binding.txtLabelStart.visibility = View.VISIBLE
            binding.txtLabelStart.text = labelStart
        }

        if (labelEnd.isNotEmpty()) {
            binding.txtLabelEnd.visibility = View.VISIBLE
            binding.txtLabelEnd.text = labelEnd
        }

        addRankingViews()
    }

    private fun addRankingViews() {
        for (i in 1..maxSize) {
            val number = i
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.view_feedback_ranking_item, binding.rankingContainer, false)
            val txtRanking = view.findViewById<TextView>(R.id.txt_ranking)
            txtRanking.text = number.toString()
            txtRanking.setOnClickListener { v ->
                unselectAll()
                v.isSelected = true
                currentRanking = number
                listener?.onCurrentRankingChange(this, currentRanking)
            }

            val params = LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT, 1f)
            binding.rankingContainer.addView(view, params)
        }
    }

    private fun unselectAll() {
        var i = 0
        val size = binding.rankingContainer.childCount
        while (i < size) {
            binding.rankingContainer.getChildAt(i).findViewById<View>(R.id.txt_ranking).isSelected = false
            i++
        }
    }

    fun getCurrentRanking(): Int {
        return currentRanking
    }

    fun setCurrentRanking(currentRanking: Int) {
        if (currentRanking <= 0) {
            unselectAll()
        } else if (currentRanking <= binding.rankingContainer.childCount) {
            unselectAll()
            val view = binding.rankingContainer.getChildAt(currentRanking - 1)
            view.isSelected = true
            this.currentRanking = currentRanking
        }
    }

    interface OnCurrentRankingChangeListener {
        fun onCurrentRankingChange(view: FeedbackRankingView, currentRanking: Int)
    }

    companion object {

        const val DEFAULT_MAX_SIZE = 5

        @InverseBindingAdapter(attribute = "currentRanking")
        @JvmStatic
        fun getCurrentRanking(view: FeedbackRankingView): Int {
            return view.getCurrentRanking()
        }

        @BindingAdapter("currentRanking")
        @JvmStatic
        fun setCurrentRanking(view: FeedbackRankingView, currentRanking: Int) {
            if (currentRanking != view.getCurrentRanking()) {
                view.setCurrentRanking(currentRanking)
            }
        }

        @BindingAdapter("currentRankingAttrChanged")
        @JvmStatic
        fun setCurrentRankingAttrChanged(view: FeedbackRankingView, listener: InverseBindingListener?) {
            view.listener = listener?.let {
                object : OnCurrentRankingChangeListener {
                    override fun onCurrentRankingChange(view: FeedbackRankingView, currentRanking: Int) {
                        it.onChange()
                    }
                }
            }
        }
    }

}
