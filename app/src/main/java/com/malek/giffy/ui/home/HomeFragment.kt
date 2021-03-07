package com.malek.giffy.ui.home

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.malek.giffy.R
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

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
                Glide
                    .with(this)
                    .asGif()
                    .listener(object : RequestListener<GifDrawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressCircular.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: GifDrawable?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressCircular.visibility = View.GONE
                            return false

                        }

                    })
                    .load(gif)
                    .into(preview)
            }
            it.errorString?.let { resId ->
                Snackbar.make(
                    root,
                    getString(resId),
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.retry) {
                    homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
                }.show()


            }
        })
        return root
    }
}
