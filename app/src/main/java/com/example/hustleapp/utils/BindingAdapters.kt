package com.example.hustleapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.hustleapp.R

/**
 * Custom binding adapters for data binding in XML layouts
 */
class BindingAdapters {
    companion object {
    
    /**
     * Load image from URL using Glide
     */
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.ic_placeholder_image)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_placeholder_image)
                .into(imageView)
        }
    }
    
    /**
     * Load circular avatar image
     */
    @JvmStatic
    @BindingAdapter("avatarUrl")
    fun loadAvatar(imageView: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.ic_default_avatar)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(imageView)
        }
    }
    
    /**
     * Format and display relative time
     */
    @JvmStatic
    @BindingAdapter("timeAgo")
    fun setTimeAgo(textView: TextView, timestamp: Long?) {
        timestamp?.let {
            textView.text = DateUtils.getTimeAgo(it)
        }
    }
    
    /**
     * Format and display date range
     */
    @JvmStatic
    @BindingAdapter("startDate", "endDate")
    fun setDateRange(textView: TextView, startDate: Long?, endDate: Long?) {
        startDate?.let {
            textView.text = DateUtils.formatDateRange(it, endDate)
        }
    }
    
    /**
     * Format and display date
     */
    @JvmStatic
    @BindingAdapter("formattedDate")
    fun setFormattedDate(textView: TextView, timestamp: Long?) {
        timestamp?.let {
            textView.text = DateUtils.formatDate(it)
        }
    }
    
    /**
     * Set visibility based on boolean (visible/gone)
     */
    @JvmStatic
    @BindingAdapter("visibleOrGone")
    fun setVisibleOrGone(view: android.view.View, visible: Boolean) {
        view.visibility = if (visible) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    /**
     * Set visibility based on boolean (visible/invisible)
     */
    @JvmStatic
    @BindingAdapter("visibleOrInvisible")
    fun setVisibleOrInvisible(view: android.view.View, visible: Boolean) {
        view.visibility = if (visible) android.view.View.VISIBLE else android.view.View.INVISIBLE
        }
}
}
