package com.example.composetemplate.presentation.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composetemplate.presentation.common.FullScreenProgressBar
import com.example.composetemplate.presentation.common.LoginPageHeader
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreen
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreenStateManagement
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.RegisterScreen
import com.example.composetemplate.ui.theme.LoginScreenColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTemplateTheme {
                // set the system bar color to what I want
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setSystemBarsColor(
                        color = LoginScreenColor,
                    )
                }
                Login()
            }
        }
    }

    @Composable
    fun Login(
        authViewModel: AuthViewModel = koinViewModel()
    ){
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var currentScreen by remember { mutableStateOf(LoginScreenStateManagement.Login) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LoginScreenColor),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FullScreenProgressBar(authViewModel.isProgressVisible)
            LoginPageHeader()
            when (currentScreen) {
                LoginScreenStateManagement.Login -> LoginScreen(
                    viewModel = authViewModel,
                    onRegisterClicked = { currentScreen = LoginScreenStateManagement.Register }
                )
                LoginScreenStateManagement.Register -> RegisterScreen(
                    viewModel = authViewModel,
                    onLoginClicked = { currentScreen = LoginScreenStateManagement.Login }
                )
            }
        }
    }

}