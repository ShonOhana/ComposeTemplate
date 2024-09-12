package com.example.composetemplate.presentation.screens.main_screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.components.BasicScreen
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigator: Navigator,
    vm:MainViewModel,
    authViewModel: AuthViewModel = koinViewModel()
){
    LaunchedEffect(Unit) {
        vm.getDataFromServer()
        vm.saveDataToDB()
        vm.writeDataToDS()
        // TODO: remove when we get design to logout,
        //  meanwhile we logged out every exit of the screen because we work on auth screen
        authViewModel.logOut()
    }
    BasicScreen(Color.Magenta,"Home"){
        navigator.navigate(MainScreens.Favorites)
    }
}