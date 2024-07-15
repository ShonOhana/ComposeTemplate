package com.example.composetemplate.presentation.screens.entry_screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.EntryScreens
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun RegisterScreen(navigator: Navigator, shouldSowAdd: Boolean) {
    val nextDestination = if (shouldSowAdd) {
        EntryScreens.Advertisement
    } else {
        MainScreens.Home
    }

    BasicScreen(Color.Blue, "Register") {
        navigator.navigate(nextDestination)
    }
}