package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.databinding.FragmentInformationBinding
import io.github.droidkaigi.confsched2017.viewmodel.InformationViewModel

class InformationFragment : BaseFragment() {

    private lateinit var binding: FragmentInformationBinding

    @Inject
    lateinit var viewModel: InformationViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    companion object {
        val TAG: String = InformationFragment::class.java.simpleName

        fun newInstance() = InformationFragment()
    }
}
