package com.malek.giffy.ui.details

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.malek.giffy.MainViewModel
import com.malek.giffy.R
import com.malek.giffy.utilities.showGIF


class GIFDetailsFragment : DialogFragment() {
    private val gifDetailsFragmentArgs: GIFDetailsFragmentArgs by navArgs()
    private val activityViewModel by activityViewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.FullScreenDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityViewModel.shouldHideNavView()

        view.findViewById<ImageView>(R.id.image_details)?.apply {
            this.showGIF(
                    fullScreen = true,
                    progressBar = view.findViewById(R.id.progress_circular),
                    imageUrl = gifDetailsFragmentArgs.gifImageUrl,
                    placeholder = null
            )
        }
        view.findViewById<MaterialToolbar>(R.id.toolbar)?.let {
            it.title = gifDetailsFragmentArgs.gifName
            (activity as? AppCompatActivity)?.setSupportActionBar(it)
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.setNavigationOnClickListener {
                (activity as? AppCompatActivity)?.setSupportActionBar(null)
                activityViewModel.shouldShowNavView()
                dismiss()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, gifDetailsFragmentArgs.gifImageUrl)
            startActivity(Intent.createChooser(i, "Share URL"))
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activityViewModel.shouldShowNavView()
    }

}