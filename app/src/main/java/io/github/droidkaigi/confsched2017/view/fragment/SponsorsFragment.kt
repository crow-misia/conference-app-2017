package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.databinding.ObservableList
import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentSponsorsBinding
import io.github.droidkaigi.confsched2017.databinding.ViewSponsorCellBinding
import io.github.droidkaigi.confsched2017.databinding.ViewSponsorshipCellBinding
import io.github.droidkaigi.confsched2017.view.activity.SponsorsActivity
import io.github.droidkaigi.confsched2017.view.customview.BindingHolder
import io.github.droidkaigi.confsched2017.view.customview.ObservableListRecyclerAdapter
import io.github.droidkaigi.confsched2017.viewmodel.SponsorViewModel
import io.github.droidkaigi.confsched2017.viewmodel.SponsorshipViewModel
import io.github.droidkaigi.confsched2017.viewmodel.SponsorshipsViewModel

class SponsorsFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: SponsorshipsViewModel

    private lateinit var binding: FragmentSponsorsBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSponsorsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        initView()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.destroy()
    }

    private fun initView() {
        val adapter = SponsorshipsAdapter(context, viewModel.sponsorShipViewModels)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private inner class SponsorAdapter(context: Context, list: ObservableList<SponsorViewModel>) : ObservableListRecyclerAdapter<SponsorViewModel, BindingHolder<ViewSponsorCellBinding>>(context, list) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder<ViewSponsorCellBinding>(context, parent, R.layout.view_sponsor_cell)

        override fun onBindViewHolder(holder: BindingHolder<ViewSponsorCellBinding>, position: Int) {
            val viewModel = getItem(position)
            val itemBinding = holder.binding
            itemBinding.sponsorLogo.minimumHeight = screenWidth / 3
            itemBinding.viewModel = viewModel
            itemBinding.executePendingBindings()
        }

        private val screenWidth: Int
            get() {
                val display = (context as SponsorsActivity).windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                return size.x
            }
    }

    private inner class SponsorshipsAdapter(context: Context, list: ObservableList<SponsorshipViewModel>) : ObservableListRecyclerAdapter<SponsorshipViewModel, BindingHolder<ViewSponsorshipCellBinding>>(context, list) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder<ViewSponsorshipCellBinding>(context, parent, R.layout.view_sponsorship_cell)

        override fun onBindViewHolder(holder: BindingHolder<ViewSponsorshipCellBinding>, position: Int) {
            val viewModel = getItem(position)
            val itemBinding = holder.binding
            val adapter = SponsorAdapter(holder.itemView.context, viewModel.sponsorViewModels)
            itemBinding.sponsorshipRecyclerView.adapter = adapter
            itemBinding.viewModel = viewModel
            itemBinding.executePendingBindings()
        }
    }

    companion object {
        val TAG: String = SponsorsFragment::class.java.simpleName

        fun newInstance() = SponsorsFragment()
    }
}
