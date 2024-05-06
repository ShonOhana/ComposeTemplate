package com.example.composetemplate.di

import com.example.composetemplate.MainApplication
import org.koin.dsl.module

val appModule = module {
    single { MainApplication() }
}