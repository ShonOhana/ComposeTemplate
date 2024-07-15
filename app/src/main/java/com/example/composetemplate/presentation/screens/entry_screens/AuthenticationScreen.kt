package com.example.composetemplate.presentation.screens.entry_screens

import androidx.compose.runtime.Composable
import com.example.composetemplate.navigation.EntryScreens
import com.example.composetemplate.navigation.Navigator

@Composable
fun AuthenticationScreen(args: EntryScreens.Authentication, navigator: Navigator) {
    if (args.isAuthorized) {
        LogInScreen(navigator, args.shouldShowAdd)
    } else {
        RegisterScreen(navigator, args.shouldShowAdd)
    }
}