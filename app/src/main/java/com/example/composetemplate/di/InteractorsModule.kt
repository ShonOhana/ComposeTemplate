package com.example.composetemplate.di

import com.example.composetemplate.repositories.AuthInteractor
import org.koin.dsl.module


val interactorsModule = module {
    factory { AuthInteractor(get()) }
}