package com.example.composetemplate.presentation.common

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.composetemplate.R
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.utils.Constants.Companion.AUTH_WITH_GOOGLE
import com.plcoding.credentialmanagerguidecompose.GoogleCredentialManager
import kotlinx.coroutines.launch

@Composable
fun LoginPageHeader(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val accountManager = remember {
        GoogleCredentialManager()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.40f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.ono_lecture_image),
                contentDescription = "Image",
                modifier = Modifier
                    .width(133.dp)
                    .height(80.dp)
                    .weight(1f) // Distribute available space evenly// Adjust size as needed
            )

            // Display the image
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.google_login_icon),
                contentDescription = "Image",
                modifier = Modifier
                    .size(55.dp)
                    .clickable {
                        scope.launch {
//                            accountManager.signUp("Shon Ohana", "123456")
//                            accountManager.signIn()
                        }
                    }
            )

            Text(
                modifier = modifier
                    .padding(top = 6.dp),
                text = AUTH_WITH_GOOGLE,
                color = CustomTheme.colors.text,
                style = MaterialTheme.typography.labelLarge
            )

        }
    }
}
