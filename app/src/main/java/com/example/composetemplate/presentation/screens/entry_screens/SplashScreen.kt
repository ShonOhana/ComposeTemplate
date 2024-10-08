package com.example.composetemplate.presentation.screens.entry_screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.EntryScreens
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navigator: Navigator,
    authViewModel: AuthViewModel = koinViewModel()
) {
    BasicScreen(Color.Yellow, "Splash")
    LaunchedEffect(Unit) {
        val fiveSeconds = 5000L
        delay(fiveSeconds)
        // parameter to pass data for example
        val isUserAuthorized = false
        val shouldShowAdd = true
        /** We will pass `isUserAuthorized` and `shouldShowAdd` values to the Authentication screen with its navigation route.
         * This is one way to pass data to another screen.
         *
         * Advantages:
         * 1. Very easy and straightforward.
         * 2. Supports saved state handle by default.
         *
         * Disadvantages:
         * 1. Can't share complex data.
         * 2. If you have data that may be relevant for multiple screens, you will have to chain it across all screens.
         * 3. The data does not change dynamically.
         *
         * Therefore, if you wish to share complex data like a large list of objects or if your data is relevant for multiple screens, use the ViewModel.
         * @see com.example.composetemplate.presentation.screens.main_screens.MainViewModel
         */
        authViewModel.getUser { user, exception ->
            if (user != null && exception == null)
                navigator.navigate(MainScreens.Home)
            else
                navigator.navigate(EntryScreens.Auth)
        }

    }
}