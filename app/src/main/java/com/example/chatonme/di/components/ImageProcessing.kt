package com.example.chatonme.di.components

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.chatonme.R

/**
 * Class that manages image processing using glide.
 */
 class ImageProcessing(val context: Context) {

     /**
      * Handles images from project resource
      */
     fun setImageCenterFit(source: Int, target: ImageView) {
         Glide
             .with(context)
             .load(source)
             .fitCenter()
             .into(target)
     }

     /**
      * Handles images from database
      */
    fun setImage(url: String, target: ImageView){
        Glide
            .with(context)
            .load(url)
            .error(R.drawable.ic_account_circle)
            .placeholder(R.drawable.ic_account_circle)
            .centerCrop()
            .apply(RequestOptions.circleCropTransform())
            .into(target)
    }


}