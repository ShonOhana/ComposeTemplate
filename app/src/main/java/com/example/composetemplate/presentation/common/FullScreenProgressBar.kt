package com.example.composetemplate.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetemplate.ui.theme.CustomTheme

@Composable
fun FullScreenProgressBar(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent background
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = CustomTheme.colors.text,
                strokeWidth = 4.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FullScreenProgressBarPrev() {
    FullScreenProgressBar(isLoading = true)
}