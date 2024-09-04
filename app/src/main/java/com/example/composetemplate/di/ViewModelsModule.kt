package com.example.composetemplate.di

import com.example.composetemplate.presentation.screens.entry_screens.login.LoginViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.RegisterViewModel
import com.example.composetemplate.presentation.screens.main_screens.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModules = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get(),get()) }
}
