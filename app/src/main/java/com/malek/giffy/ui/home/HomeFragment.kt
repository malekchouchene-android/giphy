package com.malek.giffy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malek.giffy.R
import com.malek.giffy.utilities.displaySnackBarError
import com.malek.giffy.utilities.showGIF
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val preview: ImageView = root.findViewById(R.id.preview)
        val swipeToRefresh: SwipeRefreshLayout = root.findViewById(R.id.swipe_to_refresh)
        val progressCircular: ProgressBar = root.findViewById(R.id.progress_circular)

        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)

        swipeToRefresh.setOnRefreshListener {
            homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        }

        homeViewModel.state.observe(viewLifecycleOwner, Observer {
            swipeToRefresh.isRefreshing = it.isLoading
            if (it.isLoading && progressCircular.visibility == View.GONE) {
                progressCircular.visibility = View.VISIBLE
            }
            it.imageUrl?.let { gif ->
                preview.showGIF(fullScreen = true, progressBar = progressCircular, imageUrl = gif,placeholder = null)
            }
            it.errorString?.let { resId ->
                progressCircular.visibility = View.GONE
                displaySnackBarError(
                    messageStringRes = resId,
                    actionTitle = R.string.retry,
                    action = View.OnClickListener {
                        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
                    },
                    root = root
                )
            }
        })
        return root
    }
}


