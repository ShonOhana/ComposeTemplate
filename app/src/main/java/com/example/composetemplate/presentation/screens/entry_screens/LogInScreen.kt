package com.example.composetemplate.presentation.screens.entry_screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.EntryRoute
import com.example.composetemplate.navigation.MainRoute
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun LogInScreen(navigator: Navigator, shouldSowAdd: Boolean) {
    val nextDestination = if (shouldSowAdd) {
        EntryRoute.Advertisement
    } else {
        MainRoute.Home
    }

    BasicScreen(Color.Red, "Login") {
        navigator.navigate(nextDestination)
    }
}