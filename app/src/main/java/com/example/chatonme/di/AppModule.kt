package com.example.chatonme.di

import com.example.chatonme.models.AppViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module{
        single { Messaging(androidContext()) }
        single { ImageProcessing(androidContext()) }
        single { CustomDialog() }
        single { UserListAdapter(androidContext()) }
        viewModel { AppViewModel() }
    }
}