package com.example.composetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Navigator class manages the navigation operations within the application.
 * It uses a NavHostController to navigate between different routes and handles
 * the back stack appropriately to ensure a smooth user experience.
 */
class Navigator {

    lateinit var navHostController: NavHostController
        private set

    /**
     * Creates the navigation graph using the NavHostController.
     * This function should be called within a Composable context.
     */
    @Composable
    fun CreateAppNavigation() {
        navHostController = rememberNavController()
        RootNavHost(navHostController,this)
    }

    /**
     * Navigates to a specified route and handles the back stack accordingly.
     *
     * @param navigableScreen The destination route to navigate to.
     */
    fun navigate(navigableScreen: NavigableScreen) {

        when(navigableScreen.screen){
            AppScreen.Auth -> {
                navHostController.navigate(navigableScreen){
                    /* When the app navigates from the splash screen to authentication screen,
                     * we want to the remove the splash screen (startDestinationId) from the back stack */
                    popUpTo(navHostController.graph.startDestinationId){inclusive = true}
                }
            }
            AppScreen.Home -> {
                /* When the app navigates to the home screen we want to remove all the EntryGraph from the back stack */
                navigateAndRemoveGraph<EntryGraph>(navigableScreen)
            }
            AppScreen.Splash,
            AppScreen.Advertisement,
            AppScreen.Favorites ->
                /* Simply navigate to the specified route without altering the back stack. */
                navHostController.navigate(navigableScreen)

        }
    }


    /**
     * Navigates to a specified route and removes the current graph from the back stack.
     * This is particularly useful for navigating to a new screen and clearing the previous
     * graph to prevent users from navigating back to it.
     *
     * @param T The type of the graph to be removed.
     * @param navigableScreen The destination route to navigate to.
     */
    private inline fun <reified T : Graph>navigateAndRemoveGraph(navigableScreen: NavigableScreen){
        navHostController.navigate(navigableScreen){
            popUpTo<T>{
                inclusive = true
            }
        }
    }
}