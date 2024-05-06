package com.example.composetemplate.di

import com.example.composetemplate.database.dao.TestDao
import com.example.composetemplate.database.local.AppDatabase
import org.koin.dsl.module


val databaseModule = module {
    single {
        AppDatabase.getDatabase(get())
    }

    single<TestDao> {
        AppDatabase.getDatabase(get()).testDao()
    }
}