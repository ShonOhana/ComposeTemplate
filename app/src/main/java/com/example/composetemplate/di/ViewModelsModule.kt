package com.example.composetemplate.di

import com.example.composetemplate.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModules = module {
    viewModel {
        MainActivityViewModel(get())
    }
}
