package com.example.composetemplate.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import org.koin.androidx.compose.getViewModel

/**
 * Retrieves a shared ViewModel instance scoped to the parent navigation graph's back stack entry.
 *
 * @param navController The NavHostController to retrieve the parent back stack entry.
 * @return An instance of the shared ViewModel of type [T].
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    /* Retrieve the route of the parent navigation graph */
    val navGraphRoute = destination.parent?.route ?: return getViewModel()

    /* Remember the parent back stack entry to avoid recomposition */
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    /* Retrieve the shared ViewModel instance using Koin's getViewModel function */
    return getViewModel(viewModelStoreOwner = parentEntry)
}