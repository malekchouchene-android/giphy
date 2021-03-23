package com.malek.giffy.utilities

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.malek.giffy.R
import kotlin.random.Random

@BindingAdapter(
    "imageUrl",
    "fullscreen",
    "progressBar",
    "placeHolder",
    "imageDrawable",
    requireAll = false
)
fun showGif(
    imageView: ImageView,
    imageUrl: String?,
    fullScreen: Boolean,
    progressBar: ProgressBar?,
    placeholder: Drawable?,
    imageDrawable: Int?
) {
    imageUrl?.let {
        imageView.showGIF(fullScreen, progressBar, placeholder = placeholder, imageUrl = it)
    } ?: run {
        if (imageDrawable != null) imageView.showGIF(
            fullScreen,
            progressBar,
            placeholder = placeholder,
            imageUrl = null,
            imageDrawable = imageDrawable
        )

    }
}

fun ImageView.showGIF(
    fullScreen: Boolean,
    progressBar: ProgressBar?,
    @RawRes imageDrawable: Int? = null,
    placeholder: Drawable?,
    imageUrl: String?
) {
    if (progressBar?.visibility == View.GONE) progressBar.visibility = View.VISIBLE
    Glide.with(this)
        .asGif()
        .apply {
            if (fullScreen) this.fitCenter()
            if (placeholder != null) {
                this.placeholder(placeholder)
            }
            if (progressBar != null) {
                this.listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false

                    }

                })
            }
        }
        .error(randomErrorGif())
        .load(imageUrl ?: imageDrawable)
        .into(this)
}