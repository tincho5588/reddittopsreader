package com.tincho5588.reddittopsreader.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tincho5588.reddittopsreader.R

@BindingAdapter("app:imageUrl")
fun imageUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .error(R.drawable.ic_image_not_supported_black)
        .into(view)
}

@BindingAdapter("app:imageUrlWithLoadingPlaceholder")
fun imageUrlWithLoadingPlaceholder(view: ImageView, url: String) {
    val circularProgress = CircularProgressDrawable(view.context)
    circularProgress.strokeWidth = 20f
    circularProgress.centerRadius = 100f
    circularProgress.start()

    Glide.with(view.context)
        .load(url)
        .placeholder(circularProgress)
        .error(R.drawable.ic_image_not_supported_black)
        .into(view)
}

@BindingAdapter("app:seen")
fun seen(view: ImageView, seen: Boolean) {
    ContextCompat.getDrawable(
        view.context,
        R.drawable.ic_new_black
    )
    view.visibility = if (seen) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("app:createHoursAgo")
fun createdHoursAgo(view: TextView, createdUtc: Long) {
    view.text =
        view.context.getString(
            R.string.created_hours_ago,
            Utils.calculateCreatedTimeHours(createdUtc).toString()
        )
}
