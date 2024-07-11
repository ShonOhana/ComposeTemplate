package com.example.composetemplate.navigation

import kotlinx.serialization.Serializable

/**
 * NavigableScreen interface represents a navigable destination within the application.
 * Each screen corresponds to a specific screen or feature in the app.
 */
interface NavigableScreen {
    val screen:AppScreen
}

/**
 * AppScreen enum defines the different navigation destinations available in the application.
 * Each entry in the enum represents a unique screen or feature that can be navigated to.
 * If you want to add a new screen, add it in the enum as well.
 */
enum class AppScreen{
    Splash,
    Authentication,
    Advertisement,
    Home,
    Favorites
}

/**
 * EntryScreens is a sealed class that represents the routes in the entry flow of the application.
 * It implements the NavigableScreen interface and defines specific entry-related screens.
 */
@Serializable
sealed class EntryScreens : NavigableScreen {
    @Serializable
    data object Splash : EntryScreens() {
        override val screen: AppScreen
            get() = AppScreen.Splash
    }

    /* Authentication route represents the authentication screen of the application.
     * It contains additional arguments to indicate the authorization status and whether to show an advertisement.
     */
    @Serializable
    data class Authentication(
        val isAuthorized: Boolean,
        val shouldShowAdd: Boolean
    ) : EntryScreens() {
        override val screen: AppScreen
            get() = AppScreen.Authentication
    }

    @Serializable
    data object Advertisement : EntryScreens() {
        override val screen: AppScreen
            get() = AppScreen.Advertisement
    }
}

/* MainRoute is a sealed class that represents the routes in the main flow of the application.
 * It implements the Route interface and defines specific main-related routes.
 */
@Serializable
sealed class MainScreens : NavigableScreen {
    @Serializable
    data object Home : NavigableScreen {
        override val screen: AppScreen
            get() = AppScreen.Home
    }

    @Serializable
    data object Favorites : NavigableScreen {
        override val screen: AppScreen
            get() = AppScreen.Favorites
    }
}