package com.example.composetemplate.presentation.screens.entry_screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.MainRoute
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun AdvertisementScreen(navigator: Navigator) {
    BasicScreen(Color.Cyan, "Advertisement") {
        navigator.navigate(MainRoute.Home)
    }
}