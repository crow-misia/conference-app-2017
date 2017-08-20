package io.github.droidkaigi.confsched2017.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.view.fragment.SessionDetailFragmentBuilder
import timber.log.Timber

class SessionDetailActivity : BaseActivity() {

    private var parentClass: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_session_detail)
        component.inject(this)

        val sessionId = intent.getIntExtra(EXTRA_SESSION_ID, 0)
        val parentClassName = intent.getStringExtra(EXTRA_PARENT)
        if (TextUtils.isEmpty(parentClassName)) {
            parentClass = MainActivity::class.java
        } else {
            try {
                parentClass = Class.forName(parentClassName)
            } catch (e: ClassNotFoundException) {
                Timber.e(e)
            }

        }
        replaceFragment(SessionDetailFragmentBuilder(sessionId).build(), R.id.content_view)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                upToParentActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun upToParentActivity() {
        val upIntent = Intent(applicationContext, parentClass)
        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(upIntent)
        finish()
    }

    companion object {
        const val EXTRA_SESSION_ID = "session_id"
        const val EXTRA_PARENT = "parent"

        fun createIntent(context: Context, sessionId: Int, parentClass: Class<out Activity>?): Intent {
            val intent = Intent(context, SessionDetailActivity::class.java)
            intent.putExtra(EXTRA_SESSION_ID, sessionId)
            if (parentClass != null) {
                intent.putExtra(EXTRA_PARENT, parentClass.name)
            }
            return intent
        }
    }

}
