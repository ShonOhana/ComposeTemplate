package com.example.composetemplate.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTemplateTheme {
//                Navigator().CreateAppNavigation()

                // set the system bar color to what I want
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Black,
                    )
                }

                LoginScreen(
                    onRegisterPagePageClick = {
//                        navHostController.navigateSingleTopTo(Register.route)
                    }
                )

//                RegisterScreen(
//                    onRegisterPagePageClick = {
////                        navHostController.navigateSingleTopTo(Login.route)
//                    }
//                )

            }
        }
    }
}