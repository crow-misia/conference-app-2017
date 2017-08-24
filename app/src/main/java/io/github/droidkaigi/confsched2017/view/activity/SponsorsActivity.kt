package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivitySponsorsBinding
import io.github.droidkaigi.confsched2017.view.fragment.SponsorsFragment

class SponsorsActivity : BaseActivity() {

    private lateinit var binding: ActivitySponsorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sponsors)
        component.inject(this)

        initBackToolbar(binding.toolbar)
        replaceFragment(SponsorsFragment.newInstance(), R.id.content_view)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SponsorsActivity::class.java)
    }
}
