package com.example.chatonme.di.components

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatonme.R
import com.squareup.picasso.Picasso
import java.net.URL

 class ImageProcessing(val context: Context) {

    fun setImage(url: String, target: ImageView){
        Glide.with(context)
            .load(url)
            .error(R.drawable.ic_person)
            .placeholder(R.drawable.ic_person)
            .centerCrop()
            .into(target)
    }


}