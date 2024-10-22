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
    Auth,
    Advertisement,
    Home,
    Lectures,
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

    @Serializable
    data object Auth : EntryScreens() {
        override val screen: AppScreen
            get() = AppScreen.Auth
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

    @Serializable
    data object Lectures : NavigableScreen {
        override val screen: AppScreen
            get() = AppScreen.Lectures
    }
}