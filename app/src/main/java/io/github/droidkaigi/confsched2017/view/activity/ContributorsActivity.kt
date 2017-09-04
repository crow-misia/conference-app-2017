package io.github.droidkaigi.confsched2017.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.ActivityContributorsBinding
import io.github.droidkaigi.confsched2017.util.intentFor
import io.github.droidkaigi.confsched2017.view.fragment.ContributorsFragment
import io.github.droidkaigi.confsched2017.viewmodel.ToolbarViewModel

class ContributorsActivity : BaseActivity() {

    private lateinit var binding: ActivityContributorsBinding

    @Inject
    internal lateinit var viewModel: ToolbarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contributors)
        binding.viewModel = viewModel

        initBackToolbar(binding.toolbar)
        viewModel.toolbarTitle = getString(R.string.contributors)
        replaceFragment(ContributorsFragment.newInstance(), R.id.content_view)
    }

    companion object {
        fun createIntent(context: Context) = context.intentFor<ContributorsActivity>()
    }
}
