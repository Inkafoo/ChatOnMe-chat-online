package com.example.chatonme.di.components

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

class CustomDialog {
    fun materialDialog(context: Context): MaterialDialog {
        return MaterialDialog(context)
    }
}