package com.example.composetemplate.presentation.screens.main_screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun HomeScreen(navigator: Navigator,vm:MainViewModel){
    LaunchedEffect(Unit) {
        vm.getDataFromServer()
        vm.saveDataToDB()
        vm.writeDataToDS()
    }
    BasicScreen(Color.Magenta,"Home"){
        navigator.navigate(MainScreens.Favorites)
    }
}