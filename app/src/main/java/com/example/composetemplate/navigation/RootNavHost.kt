package com.example.composetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.composetemplate.presentation.screens.entry_screens.AdvertisementScreen
import com.example.composetemplate.presentation.screens.entry_screens.AuthenticationScreen
import com.example.composetemplate.presentation.screens.main_screens.MainViewModel
import com.example.composetemplate.presentation.screens.entry_screens.SplashScreen
import com.example.composetemplate.presentation.screens.main_screens.HomeScreen
import com.example.composetemplate.presentation.screens.main_screens.FavoritesScreen
import com.example.composetemplate.utils.extensions.sharedViewModel
import kotlinx.coroutines.launch

/**
 * RootNavHost is a composable function that sets up the main navigation host for the application.
 *
 * @param navHostController An instance of NavHostController used to control the navigation within the app.
 * @param navigator An instance of Navigator which manage the navigation actions.
 *
 * The function initializes the root navigation graph (the parent of all the sub graphs) using the NavHost composable and defines two
 * sub-navigation graphs: entryNavGraph and mainNavGraph.
 * Each graph represents a flow of screens.
 * @see entryNavGraph for example.
 */
@Composable
fun RootNavHost(
    navHostController: NavHostController,
    navigator: Navigator
) {

    /* The root navigation graph. The parent of all the sub-navigation graphs */
    NavHost(navController = navHostController, startDestination = Graphs.EntryGraph) {
        entryNavGraph(navigator = navigator)
        mainNavGraph(navigator = navigator)
    }
}


/**
 * entryNavGraph defines the sub-navigation graph for the entry flow.
 * This function sets up the navigation routes for the entry flow, which includes:
 * - Splash screen
 * - Authentication screen
 * - Advertisement screen
 */
fun NavGraphBuilder.entryNavGraph(
    navigator: Navigator
) {
    navigation<Graphs.EntryGraph>(startDestination = EntryRoute.Splash) {
        composable<EntryRoute.Splash> { SplashScreen(navigator) }
        composable<EntryRoute.Authentication> {
            /* Authentication is a data class that contains arguments.
             * Here is an example for getting the data from the route and passing it to the screen composable function. */
            val args = it.toRoute<EntryRoute.Authentication>()
            AuthenticationScreen(args, navigator)
        }
        composable<EntryRoute.Advertisement> { AdvertisementScreen(navigator) }
    }
}

/**
 * mainNavGraph defines the sub-navigation graph for the main flow.
 * This function sets up the navigation routes for the main flow, which includes:
 * - Splash screen
 * - Home screen
 * - Favorites screen
 */
fun NavGraphBuilder.mainNavGraph(
    navigator: Navigator
) {
    navigation<Graphs.MainGraph>(startDestination = MainRoute.Home) {
        composable<MainRoute.Home> { entry ->
            val vm = entry.sharedViewModel<MainViewModel>(navController = navigator.navHostController)
            vm.viewModelScope.launch {  }
            HomeScreen(navigator,vm)
        }
        composable<MainRoute.Favorites> { entry ->
            val vm = entry.sharedViewModel<MainViewModel>(navController = navigator.navHostController)
            FavoritesScreen(vm) }
    }
}