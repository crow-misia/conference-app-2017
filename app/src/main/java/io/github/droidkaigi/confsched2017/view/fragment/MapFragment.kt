package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentMapBinding
import io.github.droidkaigi.confsched2017.viewmodel.MapViewModel

class MapFragment : BaseFragment() {

    private lateinit var binding: FragmentMapBinding

    @Inject
    lateinit var viewModel: MapViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentMapBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_nav -> {
                viewModel.onClickRouteMenu()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        //
    }

    companion object {
        val TAG: String = MapFragment::class.java.simpleName

        fun newInstance() = MapFragment()
    }
}
