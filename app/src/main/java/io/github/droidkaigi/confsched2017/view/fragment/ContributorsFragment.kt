package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.content.res.Configuration
import android.databinding.ObservableList
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentContributorsBinding
import io.github.droidkaigi.confsched2017.databinding.ViewContributorCellBinding
import io.github.droidkaigi.confsched2017.view.customview.ArrayRecyclerAdapter
import io.github.droidkaigi.confsched2017.view.customview.BindingHolder
import io.github.droidkaigi.confsched2017.viewmodel.ContributorViewModel
import io.github.droidkaigi.confsched2017.viewmodel.ContributorsViewModel

class ContributorsFragment : BaseFragment(), ContributorsViewModel.Callback {

    @Inject
    lateinit var viewModel: ContributorsViewModel

    private lateinit var binding: FragmentContributorsBinding

    private lateinit var adapter: Adapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onDetach() {
        viewModel.destroy()
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.callback = this
        viewModel.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentContributorsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        initView()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater?.inflate(R.menu.menu_contributors, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.item_repository -> {
            viewModel.onClickRepositoryMenu()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun initView() {
        adapter = Adapter(context, viewModel.contributorViewModels)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, columnCount)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        (binding.recyclerView.layoutManager as GridLayoutManager).spanCount = columnCount
    }

    override fun showError(@StringRes textRes: Int) {
        Snackbar.make(binding.root, textRes, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { _ -> viewModel.retry() }
                .setActionTextColor(ContextCompat.getColor(activity, R.color.white))
                .show()
    }

    private val columnCount: Int
        get() = resources.getInteger(R.integer.contributors_columns)

    private class Adapter(context: Context, list: ObservableList<ContributorViewModel>) : ArrayRecyclerAdapter<ContributorViewModel, BindingHolder<ViewContributorCellBinding>>(context, list) {

        init {
            list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<ContributorViewModel>>() {
                override fun onChanged(contributorViewModels: ObservableList<ContributorViewModel>) = notifyDataSetChanged()

                override fun onItemRangeChanged(contributorViewModels: ObservableList<ContributorViewModel>, i: Int, i1: Int) = notifyItemRangeChanged(i, i1)

                override fun onItemRangeInserted(contributorViewModels: ObservableList<ContributorViewModel>, i: Int, i1: Int) = notifyItemRangeInserted(i, i1)

                override fun onItemRangeMoved(contributorViewModels: ObservableList<ContributorViewModel>, i: Int, i1: Int, i2: Int) = notifyItemMoved(i, i1)

                override fun onItemRangeRemoved(contributorViewModels: ObservableList<ContributorViewModel>, i: Int, i1: Int) = notifyItemRangeRemoved(i, i1)
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder<ViewContributorCellBinding>(context, parent, R.layout.view_contributor_cell)

        override fun onBindViewHolder(holder: BindingHolder<ViewContributorCellBinding>, position: Int) {
            val viewModel = getItem(position)
            val itemBinding = holder.binding
            itemBinding.viewModel = viewModel
            itemBinding.executePendingBindings()
        }
    }

    companion object {
        fun newInstance() = ContributorsFragment()
    }
}
