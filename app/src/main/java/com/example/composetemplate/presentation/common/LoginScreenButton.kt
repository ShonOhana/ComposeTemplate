package com.example.composetemplate.presentation.common

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetemplate.presentation.login.LoginScreenEnum
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.ui.theme.LoginButtonColorDisable
import com.example.composetemplate.ui.theme.LoginButtonColorEnable
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.tag
import com.example.composetemplate.utils.type


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
            .padding(horizontal = 16.dp)
            .height(52.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (!isEnabled) LoginButtonColorDisable else LoginButtonColorEnable
        ),
        onClick = {
            if (isEnabled) {
                onClick()
               LogsManager().logMessage(type.VERBOSE,tag.GENERAL,"on click")
            }
        },
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterScreenPrev() {
    ComposeTemplateTheme {
        LoginScreenButton(
            Modifier,
            text = "text",
            isEnabled = true,
            onClick = {}
        )
    }
}