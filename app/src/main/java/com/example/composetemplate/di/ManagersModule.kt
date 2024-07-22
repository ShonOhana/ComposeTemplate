package com.example.composetemplate.di

import com.example.composetemplate.managers.DataStoreManager
import com.example.composetemplate.managers.NetworkManager
import org.koin.dsl.module

val managersModule = module {
    factory { NetworkManager(get()) }
    factory { DataStoreManager(get()) }
}

