package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivityLicensesBinding
import io.github.droidkaigi.confsched2017.util.intentFor
import io.github.droidkaigi.confsched2017.view.fragment.LicensesFragment

class LicensesActivity : BaseActivity() {

    private lateinit var binding: ActivityLicensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_licenses)
        component.inject(this)

        initBackToolbar(binding.toolbar)
        replaceFragment(LicensesFragment.newInstance(), R.id.content_view)
    }

    companion object {
        fun createIntent(context: Context) = context.intentFor<LicensesActivity>()
    }
}
