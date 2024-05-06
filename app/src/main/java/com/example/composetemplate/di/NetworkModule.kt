package com.example.composetemplate.di

import com.example.composetemplate.data.remote.Networking
import com.example.composetemplate.data.remote.base.BaseNetworking
import org.koin.dsl.module

val networkModule = module {
    single { Networking(get()) }
    single<BaseNetworking> {
        get<Networking>()
    }
}