package com.example.composetemplate.di

import BluetoothManager
import com.example.composetemplate.managers.FirebaseConfigurationManager
import com.example.composetemplate.managers.DataStoreManager
import com.example.composetemplate.managers.MainNetworkManager
import org.koin.dsl.module

val managersModule = module {
    factory { MainNetworkManager(get()) }
    factory { DataStoreManager(get()) }
    factory { BluetoothManager(get()) }
    single { FirebaseConfigurationManager() }
}

