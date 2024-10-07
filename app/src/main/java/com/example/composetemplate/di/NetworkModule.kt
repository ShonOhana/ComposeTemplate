package com.example.composetemplate.di

import com.example.composetemplate.data.remote.Networking
import com.example.composetemplate.data.remote.base.BaseNetworking
import com.example.composetemplate.managers.TokenFetcher
import com.example.composetemplate.managers.TokenManager
import com.example.composetemplate.repositories.FirebaseDataSource
import org.koin.dsl.module

val networkModule = module {
    factory <TokenFetcher> { FirebaseDataSource() }
    single { TokenManager(get()) }


    single { Networking(get()) }
    single<BaseNetworking> {
        get<Networking>()
    }
}