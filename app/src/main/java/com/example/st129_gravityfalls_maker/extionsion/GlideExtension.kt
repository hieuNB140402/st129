package com.example.st129_gravityfalls_maker.extionsion

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.facebook.shimmer.ShimmerDrawable

fun loadImageGlide(context: Context, path: String, imageView: ImageView) {
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(SystemUtils.shimmer)
    }
    Glide.with(context).load(path).placeholder(shimmerDrawable).error(shimmerDrawable).into(imageView)
}
fun loadImageGlide(constraintLayout: ConstraintLayout, path: String, imageView: ImageView) {
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(SystemUtils.shimmer)
    }
    Glide.with(constraintLayout).load(path).placeholder(shimmerDrawable).error(shimmerDrawable).into(imageView)
}