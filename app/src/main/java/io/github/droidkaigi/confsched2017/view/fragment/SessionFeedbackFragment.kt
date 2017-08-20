package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentSessionFeedbackBinding
import io.github.droidkaigi.confsched2017.model.SessionFeedback
import io.github.droidkaigi.confsched2017.viewmodel.SessionFeedbackViewModel

@FragmentWithArgs
class SessionFeedbackFragment : BaseFragment(), SessionFeedbackViewModel.Callback {

    @Inject
    lateinit var viewModel: SessionFeedbackViewModel

    @Arg
    var sessionId: Int = 0

    private lateinit var binding: FragmentSessionFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
        viewModel.callback = this
        viewModel.findSession(sessionId)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSessionFeedbackBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDetach() {
        viewModel.destroy()
        super.onDetach()
    }

    override fun onSuccessSubmit() {
        showToast(R.string.session_feedback_submit_success)
    }

    override fun onErrorSubmit() {
        showToast(R.string.session_feedback_submit_failure)
    }

    override fun onErrorUnFilled() {
        showToast(R.string.session_feedback_error_not_filled)
    }

    override fun onSessionFeedbackInitialized(sessionFeedback: SessionFeedback) {
        val title = if (sessionFeedback.isSubmitted)
            getString(R.string.session_feedback_submitted_title, sessionFeedback.sessionTitle)
        else
            sessionFeedback.sessionTitle
        setActionBarTitle(title)
    }

    private fun setActionBarTitle(title: String) {
        when (activity) {
            is AppCompatActivity -> {
                (activity as AppCompatActivity).supportActionBar?.title = title
            }
        }
    }

    private fun showToast(@StringRes messageResId: Int) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG: String = SessionFeedbackFragment::class.java.simpleName
    }
}
