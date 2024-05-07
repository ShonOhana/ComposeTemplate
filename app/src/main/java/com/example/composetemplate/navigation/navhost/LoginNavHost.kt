package com.example.composetemplate.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composetemplate.navigation.destination.Login
import com.example.composetemplate.navigation.destination.Register
import com.example.composetemplate.utils.extensions.navigateSingleTopTo
import com.example.composetemplate.presentation.login.LoginScreen
import com.example.composetemplate.presentation.register.RegisterScreen

@Composable
fun LoginNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(navController = navHostController, startDestination = Login.route, modifier = modifier) {
        composable(Login.route) {
            LoginScreen(
                onRegisterPagePageClick = {
                    navHostController.navigateSingleTopTo(Register.route)
                }
            )
        }
        composable(Register.route) {
            RegisterScreen(
                onRegisterPagePageClick = {
                    navHostController.navigateSingleTopTo(Login.route)
                }
            )
        }
    }

}