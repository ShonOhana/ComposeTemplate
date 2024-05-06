package com.example.composetemplate.di

import com.example.composetemplate.TestRepository
import org.koin.dsl.module

val repositoriesModule = module {
    factory { TestRepository(get(),get()) }
}