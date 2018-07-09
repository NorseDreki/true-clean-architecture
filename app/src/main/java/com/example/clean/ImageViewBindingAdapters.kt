package com.example.clean

import android.databinding.BindingAdapter
import android.widget.ImageView

object ImageViewBindingAdapters {
    @BindingAdapter("src")
    @JvmStatic fun setImageViewResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @BindingAdapter("tint")
    @JvmStatic fun bindImageTint(imageView: ImageView, color: Int) {
        if (color != 0) {
            imageView.setColorFilter(color)
        } else {
            imageView.clearColorFilter()
        }
    }
}
