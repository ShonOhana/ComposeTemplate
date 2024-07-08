package com.example.composetemplate.presentation.screens.main_screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.MainRoute
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun HomeScreen(navigator: Navigator,vm:MainViewModel){
    BasicScreen(Color.Magenta,"Home"){
        vm.getDataFromServer()
        vm.saveDataToDB()
        navigator.navigate(MainRoute.Favorites)
    }
}