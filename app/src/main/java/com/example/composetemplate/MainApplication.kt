package com.example.composetemplate

import android.app.Application
import com.example.composetemplate.di.appModule
import com.example.composetemplate.di.databaseModule
import com.example.composetemplate.di.managersModule
import com.example.composetemplate.di.networkModule
import com.example.composetemplate.di.repositoriesModule
import com.example.composetemplate.di.viewModelsModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                appModule,
                networkModule,
                repositoriesModule,
                viewModelsModules,
                managersModule,
                databaseModule
            )
        }
    }

}