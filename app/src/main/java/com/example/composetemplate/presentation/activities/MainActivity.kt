package com.example.composetemplate.presentation.activities

import AuthScreen
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.ui.theme.LoginScreenColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            ComposeTemplateTheme {
                // set the system bar color to what I want
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setSystemBarsColor(
                        color = LoginScreenColor,
                    )
                }
                Navigator().CreateAppNavigation()

            }
        }
    }
}