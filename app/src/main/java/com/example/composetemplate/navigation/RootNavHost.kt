package com.example.composetemplate.navigation

import AuthScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.composetemplate.presentation.screens.main_screens.MainViewModel
import com.example.composetemplate.presentation.screens.entry_screens.SplashScreen
import com.example.composetemplate.presentation.screens.main_screens.HomeScreen
import com.example.composetemplate.presentation.screens.main_screens.FavoritesScreen
import com.example.composetemplate.presentation.screens.main_screens.LectureScreen
import com.example.composetemplate.presentation.screens.main_screens.viewmodels.LecturesViewModel
import com.example.composetemplate.utils.extensions.sharedViewModel

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
    NavHost(navController = navHostController, startDestination = EntryGraph) {
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
    navigation<EntryGraph>(startDestination = EntryScreens.Splash) {
        composable<EntryScreens.Splash> { SplashScreen(navigator) }
        composable<EntryScreens.Auth> { AuthScreen(navigator) }

        //todo: this is in comment to have a reference to pass data between screens
        //todo: SHON: delete after I pass data to different screen
//        composable<EntryScreens.Authentication> {
//            /* Authentication is a data class that contains arguments.
//             * Here is an example for getting the data from the route and passing it to the screen composable function. */
//            val args = it.toRoute<EntryScreens.Authentication>()
//            AuthenticationScreen(args, navigator)
//        }
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
    navigation<MainGraph>(startDestination = MainScreens.Lectures) {
        composable<MainScreens.Home> { entry ->
            val vm = entry.sharedViewModel<MainViewModel>(navController = navigator.navHostController)
            HomeScreen(navigator,vm)
        }
        composable<MainScreens.Lectures> { entry ->
            val vm = entry.sharedViewModel<LecturesViewModel>(navController = navigator.navHostController)
            LectureScreen(viewModel = vm)
        }
        composable<MainScreens.Favorites> { entry ->
            val vm = entry.sharedViewModel<MainViewModel>(navController = navigator.navHostController)
            FavoritesScreen(vm) }
    }
}