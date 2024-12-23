package com.example.composetemplate.presentation.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.composetemplate.managers.FirebaseConfigurationManager
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.common.LectureCard
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            ComposeTemplateTheme {
                Navigator().CreateAppNavigation()
            }
        }
    }
}