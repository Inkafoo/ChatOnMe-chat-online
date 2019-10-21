package com.example.chatonme.di.components

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatonme.R
import com.squareup.picasso.Picasso
import java.net.URL

internal class ImageProcessing(val context: Context) {


    fun setImage(url: String, target: ImageView){
        Glide.with(context)
            .load(url)
            .error(R.drawable.ic_chat)
            .placeholder(R.drawable.ic_chat)
            .centerCrop()
            .into(target)
    }


}