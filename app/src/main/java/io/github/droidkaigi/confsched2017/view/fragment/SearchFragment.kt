package io.github.droidkaigi.confsched2017.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable

import java.util.ArrayList

import javax.inject.Inject

import io.github.droidkaigi.confsched2017.R
import io.github.droidkaigi.confsched2017.databinding.FragmentSearchBinding
import io.github.droidkaigi.confsched2017.databinding.ViewSearchResultBinding
import io.github.droidkaigi.confsched2017.view.customview.ArrayRecyclerAdapter
import io.github.droidkaigi.confsched2017.view.customview.BindingHolder
import io.github.droidkaigi.confsched2017.viewmodel.SearchResultViewModel
import io.github.droidkaigi.confsched2017.viewmodel.SearchViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class SearchFragment : BaseFragment(), SearchViewModel.Callback {

    @Inject
    lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var adapter: SearchResultsAdapter

    private lateinit var binding: FragmentSearchBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.callback = this
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_search, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        menuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                val activity = activity
                activity?.onBackPressed()
                return false
            }
        })
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return onQueryTextChange(query)
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.previousSearchText = newText
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.action_search)?.expandActionView()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        initRecyclerView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onDetach() {
        compositeDisposable.dispose()
        viewModel.destroy()
        super.onDetach()
    }

    private fun initRecyclerView() {
        adapter = SearchResultsAdapter(context)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadData() {
        viewModel.getSearchResultViewModels(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { this.renderSearchResults(it) },
                        onError = { Timber.tag(TAG).e(it, "Search result load failed.") }
                ).addTo(compositeDisposable)
    }

    override fun closeSearchResultList() {
        adapter.clearAllResults()
    }

    private fun renderSearchResults(searchResultViewModels: List<SearchResultViewModel>) {
        adapter.setAllList(searchResultViewModels)
        val searchText = adapter.previousSearchText
        if (!TextUtils.isEmpty(searchText)) {
            adapter.filter.filter(searchText)
        }
    }

    private inner class SearchResultsAdapter internal constructor(context: Context) : ArrayRecyclerAdapter<SearchResultViewModel, BindingHolder<ViewSearchResultBinding>>(context), Filterable {

        private val filteredList = ArrayList<SearchResultViewModel>()

        private var allList: List<SearchResultViewModel>? = null

        internal var previousSearchText: String = ""

        init {
            setHasStableIds(true)
        }

        internal fun setAllList(viewModels: List<SearchResultViewModel>) {
            this.allList = viewModels
        }

        internal fun clearAllResults() {
            clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder<ViewSearchResultBinding>(context, parent, R.layout.view_search_result)

        override fun onBindViewHolder(holder: BindingHolder<ViewSearchResultBinding>, position: Int) {
            val viewModel = getItem(position)
            val itemBinding = holder.binding
            itemBinding.viewModel = viewModel
            itemBinding.executePendingBindings()

            itemBinding.txtSearchResult.text = viewModel.getMatchedText(previousSearchText)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                    filteredList.clear()
                    val results = Filter.FilterResults()

                    if (constraint.isNotEmpty()) {
                        val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                        allList.orEmpty().asSequence()
                                .filter { it.match(filterPattern) }
                                .forEach{ filteredList.add(it) }
                    }

                    results.values = filteredList
                    results.count = filteredList.size

                    return results
                }

                override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                    clear()
                    addAll(results.values as List<SearchResultViewModel>)
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemId(position: Int): Long {
            val viewModel = getItem(position)
            return viewModel.searchResultId.toLong()
        }
    }

    companion object {
        private val TAG = SearchFragment::class.java.simpleName

        fun newInstance() = SearchFragment()
    }
}
