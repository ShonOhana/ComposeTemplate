package com.example.composetemplate.di

import com.example.composetemplate.data.local.dao.TestDao
import com.example.composetemplate.data.local.AppDatabase
import org.koin.dsl.module


val databaseModule = module {
    single {
        AppDatabase.getDatabase(get())
    }

    single<TestDao> {
        AppDatabase.getDatabase(get()).testDao()
    }
}