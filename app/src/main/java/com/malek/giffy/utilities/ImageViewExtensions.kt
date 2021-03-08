package com.malek.giffy.utilities

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.showGIF(
    fullScreen: Boolean,
    progressBar: ProgressBar?,
    placeholder: Drawable?,
    imageUrl: String
) {
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
        .load(imageUrl)
        .into(this)
}