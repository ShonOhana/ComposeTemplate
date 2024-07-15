package com.example.composetemplate.presentation.screens.main_screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.presentation.components.BasicScreen

@Composable
fun FavoritesScreen(vm:MainViewModel){
    vm.loadDataFromDB()
    BasicScreen(Color.Gray,"Favorites")
}