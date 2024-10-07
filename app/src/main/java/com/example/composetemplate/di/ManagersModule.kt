package com.example.composetemplate.di

import com.example.composetemplate.managers.DataStoreManager
import com.example.composetemplate.managers.FirebaseNetworkManager
import com.example.composetemplate.managers.MainNetworkManager
import org.koin.dsl.module

val managersModule = module {
    factory { MainNetworkManager(get()) }
    factory { FirebaseNetworkManager(get()) }
    factory { DataStoreManager(get()) }
}

