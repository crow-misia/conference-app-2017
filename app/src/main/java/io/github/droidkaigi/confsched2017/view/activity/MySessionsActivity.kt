package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivityMySessionsBinding
import io.github.droidkaigi.confsched2017.view.fragment.MySessionsFragment

class MySessionsActivity : BaseActivity() {

    private lateinit var binding: ActivityMySessionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMySessionsBinding>(this, R.layout.activity_my_sessions)
        component.inject(this)

        initBackToolbar(binding.toolbar)
        replaceFragment(MySessionsFragment.newInstance(), R.id.content_view)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MySessionsActivity::class.java)
    }
}
