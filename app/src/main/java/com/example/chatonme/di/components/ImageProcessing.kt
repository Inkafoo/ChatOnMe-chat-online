package com.example.chatonme.di.components

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.chatonme.R


 class ImageProcessing(val context: Context) {

    fun setImage(url: String, target: ImageView){
        Glide.with(context)
            .load(url)
            .error(R.drawable.ic_account_circle_grey)
            .placeholder(R.drawable.ic_account_circle_grey)
            .apply(RequestOptions.circleCropTransform())
            .into(target)
    }


}