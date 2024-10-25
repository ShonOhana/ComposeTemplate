package com.example.composetemplate

import android.app.Application
import com.example.composetemplate.di.appModule
import com.example.composetemplate.di.coroutineModule
import com.example.composetemplate.di.dataSourceModule
import com.example.composetemplate.di.databaseModule
import com.example.composetemplate.di.interactorsModule
import com.example.composetemplate.di.managersModule
import com.example.composetemplate.di.networkModule
import com.example.composetemplate.di.repositoriesModule
import com.example.composetemplate.di.viewModelsModules
import com.example.composetemplate.managers.FirebaseConfigurationManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    private val firebaseConfigurationManager: FirebaseConfigurationManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                appModule,
                networkModule,
                coroutineModule,
                repositoriesModule,
                viewModelsModules,
                managersModule,
                databaseModule,
                dataSourceModule,
                interactorsModule,
            )
        }

        firebaseConfigurationManager.initConfiguration()
    }

}