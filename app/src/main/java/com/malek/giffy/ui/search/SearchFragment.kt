package com.malek.giffy.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malek.giffy.R
import com.malek.giffy.utilities.displaySnackBarError
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searViewModel by viewModel<SearchViewModel>()
    private lateinit var gifListAdapter: GifListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val progressCircular = view.findViewById<ProgressBar>(R.id.progress_circular)
        val imagesList = view.findViewById<RecyclerView>(R.id.images_list)
        val emptyView = view.findViewById<Group>(R.id.empty_view)
        val emptyImageView = view.findViewById<ImageView>(R.id.empty_view_image)
        val progressBarLoadMore = view.findViewById<ProgressBar>(R.id.progress_loading_more)
        initSearchView(searchView)
        initRecyclerView(recyclerView = imagesList, progressBar = progressBarLoadMore)

        searViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                SearchState.Loading -> {
                    progressCircular.visibility = View.VISIBLE
                    emptyView.visibility = View.GONE
                }
                is SearchState.ImageListStat -> {
                    progressCircular.visibility = View.GONE
                    progressBarLoadMore.visibility = View.GONE
                    emptyView.visibility = View.GONE
                    gifListAdapter.updateData(state.imageList ?: emptyList())

                }
                is SearchState.ErrorStat -> {
                    progressCircular.visibility = View.GONE
                    progressBarLoadMore.visibility = View.GONE
                    emptyView.visibility = View.GONE
                    state.errorString?.let {
                        displaySnackBarError(
                            messageStringRes = state.errorString,
                            root = view
                        )
                    }
                }
                is SearchState.EmptyStat -> {
                    progressCircular.visibility = View.GONE
                    progressBarLoadMore.visibility = View.GONE
                    emptyView.visibility = View.VISIBLE
                    state.randomEmptyGIF?.let {
                        Glide.with(this)
                            .asGif()
                            .load(it)
                            .into(emptyImageView)
                    }
                    gifListAdapter.updateData(emptyList())
                }
                SearchState.GetToEnd -> {
                    progressCircular.visibility = View.GONE
                    progressBarLoadMore.visibility = View.GONE
                }
            }
        })


    }

    private fun initSearchView(searchView: SearchView) {
        searchView.requestFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searViewModel.dispatchUserIntent(SearchUserIntent.NewQuery(query))
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun initRecyclerView(recyclerView: RecyclerView, progressBar: ProgressBar) {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        gifListAdapter = GifListAdapter()
        recyclerView.adapter = gifListAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && gifListAdapter.itemCount > 0) {
                    progressBar.visibility = View.VISIBLE
                    searViewModel.dispatchUserIntent(SearchUserIntent.NextPage)
                }
            }
        })
    }
}

