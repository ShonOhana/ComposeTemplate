package com.example.composetemplate.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.ui.theme.ComposeTemplateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTemplateTheme {
                Navigator().CreateAppNavigation()
            }
        }
    }
}