package com.example.composetemplate.di

import com.example.composetemplate.MainActivityViewModel
import com.example.composetemplate.presentation.login.LoginViewModel
import com.example.composetemplate.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModules = module {
    viewModel {
        MainActivityViewModel(get())
    }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}
