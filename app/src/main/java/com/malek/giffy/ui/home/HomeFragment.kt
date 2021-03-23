package com.malek.giffy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.malek.giffy.R
import com.malek.giffy.databinding.FragmentHomeBinding
import com.malek.giffy.utilities.displaySnackBarError
import com.malek.giffy.utilities.showGIF
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel by viewModel<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.model = homeViewModel
        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)

        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
        }


        homeViewModel.state.observe(viewLifecycleOwner, Observer { homeState ->

            homeState.errorString?.let { resId ->
                displaySnackBarError(
                    messageStringRes = resId,
                    actionTitle = R.string.retry,
                    action = View.OnClickListener {
                        homeViewModel.dispatchUserIntent(HomeUserIntent.GetNewImage)
                    },
                    root = view
                )
            }


        })

    }

}


