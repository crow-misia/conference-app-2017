package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.databinding.FragmentLicensesBinding
import io.github.droidkaigi.confsched2017.viewmodel.LicensesViewModel

class LicensesFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: LicensesViewModel

    private lateinit var binding: FragmentLicensesBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLicensesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
        viewModel.destroy()
    }

    companion object {
        fun newInstance() = LicensesFragment()
    }
}
