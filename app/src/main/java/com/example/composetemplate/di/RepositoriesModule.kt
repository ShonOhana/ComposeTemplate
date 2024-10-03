package com.example.composetemplate.di

import com.example.composetemplate.repositories.AuthDataSource
import com.example.composetemplate.repositories.AuthInteractor
import com.example.composetemplate.repositories.FirebaseDataSource
import com.example.composetemplate.repositories.LoginRepository
import com.example.composetemplate.repositories.TestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoriesModule = module {
    factory { TestRepository(get(),get()) }
    factory { AuthInteractor(get()) }
    factory <AuthDataSource> { FirebaseDataSource() }
    factory { LoginRepository(get(),get(),get()) }
}