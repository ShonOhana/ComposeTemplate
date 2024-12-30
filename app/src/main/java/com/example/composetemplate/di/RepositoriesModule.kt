package com.example.composetemplate.di

import com.example.composetemplate.repositories.AuthActionable
import com.example.composetemplate.repositories.FirebaseActionable
import com.example.composetemplate.repositories.LecturesRepository
import com.example.composetemplate.repositories.LoginRepository
import com.example.composetemplate.repositories.TestRepository
import org.koin.dsl.module

val repositoriesModule = module {
    factory { TestRepository(get(),get()) }
    factory <AuthActionable> { FirebaseActionable() }
    factory { LoginRepository(get(),get()) }
    factory { LecturesRepository(get()) }
}