package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivitySessionFeedbackBinding
import io.github.droidkaigi.confsched2017.util.intentFor
import io.github.droidkaigi.confsched2017.view.fragment.SessionFeedbackFragmentBuilder

class SessionFeedbackActivity : BaseActivity() {

    private lateinit var binding: ActivitySessionFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_feedback)
        component.inject(this)

        initBackToolbar(binding.toolbar)

        val sessionId = intent.getIntExtra(EXTRA_SESSION_ID, 0)
        replaceFragment(SessionFeedbackFragmentBuilder(sessionId).build(), R.id.content_view)
    }

    companion object {
        const val EXTRA_SESSION_ID = "session_id"

        fun createIntent(context: Context, sessionId: Int): Intent =
                context.intentFor<SessionFeedbackActivity>()
                        .putExtra(EXTRA_SESSION_ID, sessionId)
    }
}
