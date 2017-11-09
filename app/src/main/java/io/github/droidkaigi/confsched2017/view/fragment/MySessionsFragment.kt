package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentMySessionsBinding
import io.github.droidkaigi.confsched2017.databinding.ViewMySessionBinding
import io.github.droidkaigi.confsched2017.view.customview.BindingHolder
import io.github.droidkaigi.confsched2017.view.customview.ObservableListRecyclerAdapter
import io.github.droidkaigi.confsched2017.viewmodel.MySessionViewModel
import io.github.droidkaigi.confsched2017.viewmodel.MySessionsViewModel


class MySessionsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: MySessionsViewModel

    private lateinit var binding: FragmentMySessionsBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMySessionsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        initRecyclerView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.let { viewModel.start(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = MySessionAdapter(context, viewModel.mySessionViewModels)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
        }
    }

    private inner class MySessionAdapter internal constructor(context: Context, list: ObservableList<MySessionViewModel>) : ObservableListRecyclerAdapter<MySessionViewModel, BindingHolder<ViewMySessionBinding>>(context, list) {

        init {
            setHasStableIds(true)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder<ViewMySessionBinding>(context, parent, R.layout.view_my_session)

        override fun onBindViewHolder(holder: BindingHolder<ViewMySessionBinding>, position: Int) {
            holder.binding.apply {
                viewModel = getItem(position)
                executePendingBindings()
            }
        }

        override fun getItemId(position: Int) = getItem(position).mySession.id.toLong()
    }

    companion object {
        fun newInstance() = MySessionsFragment()
    }
}
