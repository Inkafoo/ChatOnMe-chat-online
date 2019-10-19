package com.example.chatonme.di.components

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.net.URL

internal class ImageProcessing(val context: Context) {

    fun setImageCenterFit(source: Uri, target: ImageView) {
        Picasso.get().load(source).fit().centerCrop().into(target)
    }

    fun setLoadedImage(url: String, target: ImageView){
        Picasso.get().load(url).fit().centerCrop().into(target)
    }


}