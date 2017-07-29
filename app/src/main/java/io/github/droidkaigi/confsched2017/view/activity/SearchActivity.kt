package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivitySearchBinding
import io.github.droidkaigi.confsched2017.view.fragment.SearchFragment

class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, R.anim.activity_fade_exit)
        binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)

        initBackToolbar(binding.toolbar)
        replaceFragment(SearchFragment.newInstance(), R.id.content_view)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.activity_fade_exit)
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }
}
