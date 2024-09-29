package com.example.composetemplate.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.composetemplate.ui.theme.CustomTheme


@Composable
fun LoginScreenButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (!isEnabled) CustomTheme.colors.loginButtonDisable else CustomTheme.colors.loginEnable
        ),
        onClick = {
            if (isEnabled) {
                onClick()
            }
        },
    ) {
        Text(
            text = text,
            color = CustomTheme.colors.text,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}