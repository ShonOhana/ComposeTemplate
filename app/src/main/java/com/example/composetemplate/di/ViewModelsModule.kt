package com.example.composetemplate.di

import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.main_screens.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModules = module {
    viewModel { AuthViewModel(get(),get()) }
    viewModel { MainViewModel(get(),get()) }
}
