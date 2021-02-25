package com.zubair.lowestest.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zubair.lowestest.R

fun getProgressDrawable(context: Context): CircularProgressDrawable =
    CircularProgressDrawable(context).apply {
        strokeWidth =10f
        centerRadius = 50f
        start()
    }

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions().placeholder(progressDrawable).error(R.drawable.ic_placeholder)
    Glide.with(context).setDefaultRequestOptions(options).load(uri).into(this)
}

//This is a binding adapter for custom attribute
@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, icon: String?){
    view.loadImage("http://openweathermap.org/img/wn/$icon@2x.png", getProgressDrawable(view.context))
}

var View.shown: Boolean get() =
    visibility == View.VISIBLE
    set(value){
        visibility = if(value) View.VISIBLE else View.GONE
    }
