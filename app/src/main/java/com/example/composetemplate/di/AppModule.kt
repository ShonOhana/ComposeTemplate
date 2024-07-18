package com.example.composetemplate.di

import com.example.composetemplate.MainApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Koin module to provide dependencies for the application.
 * This module contains a single definition that provides the MainApplication instance.
 */
val appModule = module {
    /**
     * Provides a singleton instance of MainApplication.
     * The `androidApplication()` function returns the Application context, which is cast to MainApplication.
     * This allows the MainApplication instance to be injected wherever needed in the app.
     */
    single { androidApplication() as? MainApplication }
}