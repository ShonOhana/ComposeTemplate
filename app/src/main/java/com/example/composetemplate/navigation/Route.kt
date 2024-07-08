package com.example.composetemplate.navigation

import kotlinx.serialization.Serializable

/**
 * Route interface represents a navigable destination within the application.
 * Each route corresponds to a specific screen or feature in the app.
 */
interface Route {
    val route:NavigationRoute
}

/**
 * NavigationRoute enum defines the different navigation destinations available in the application.
 * Each entry in the enum represents a unique screen or feature that can be navigated to.
 * If you want to add a new screen add it in the enum as well.
 */
enum class NavigationRoute{
    Splash,
    Authentication,
    Advertisement,
    Home,
    Favorites
}

/**
 * EntryRoute is a sealed class that represents the routes in the entry flow of the application.
 * It implements the Route interface and defines specific entry-related routes.
 */
@Serializable
sealed class EntryRoute : Route {
    @Serializable
    data object Splash : EntryRoute() {
        override val route: NavigationRoute
            get() = NavigationRoute.Splash
    }

    /* Authentication route represents the authentication screen of the application.
     * It contains additional arguments to indicate the authorization status and whether to show an advertisement.
     */
    @Serializable
    data class Authentication(
        val isAuthorized: Boolean,
        val shouldShowAdd: Boolean
    ) : EntryRoute() {
        override val route: NavigationRoute
            get() = NavigationRoute.Authentication
    }

    @Serializable
    data object Advertisement : EntryRoute() {
        override val route: NavigationRoute
            get() = NavigationRoute.Advertisement
    }
}

/* MainRoute is a sealed class that represents the routes in the main flow of the application.
 * It implements the Route interface and defines specific main-related routes.
 */
@Serializable
sealed class MainRoute : Route {
    @Serializable
    data object Home : Route {
        override val route: NavigationRoute
            get() = NavigationRoute.Home
    }

    @Serializable
    data object Favorites : Route {
        override val route: NavigationRoute
            get() = NavigationRoute.Favorites
    }
}