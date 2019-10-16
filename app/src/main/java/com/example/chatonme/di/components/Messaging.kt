package com.example.chatonme.di.components

import android.content.Context
import es.dmoral.toasty.Toasty

class Messaging(val context: Context) {

    /**
     * Shows toast message
     */
    fun showToast(message: String){
        Toasty.info(context, message, Toasty.LENGTH_SHORT).show()
    }
}