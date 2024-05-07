package com.example.composetemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.navigation.navhost.LoginNavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainActivityViewModel by inject()
        viewModel.viewModelScope.launch {

        }

        setContent {
            ComposeTemplateTheme {
                // set the system bar color to what I want
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Black,
                    )
                }

                val navHostController = rememberNavController()
                LoginNavHost(navHostController = navHostController)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTemplateTheme {
        val navHostController = rememberNavController()
        LoginNavHost(navHostController = navHostController)
    }
}