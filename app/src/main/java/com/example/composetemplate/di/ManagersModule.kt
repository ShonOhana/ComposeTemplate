package com.example.composetemplate.di

import com.example.composetemplate.managers.DataStoreManager
import com.example.composetemplate.managers.ErrorManager
import com.example.composetemplate.managers.MainNetworkManager
import org.koin.dsl.module

val managersModule = module {
    factory { MainNetworkManager(get()) }
    factory { DataStoreManager(get()) }
    single { ErrorManager() }
}

