package com.example.composetemplate.di

import com.example.composetemplate.repositories.AuthActionable
import com.example.composetemplate.repositories.FirebaseAuthManager
import org.koin.dsl.module

val dataSourceModule = module {
    factory <AuthActionable> { FirebaseAuthManager() }
}