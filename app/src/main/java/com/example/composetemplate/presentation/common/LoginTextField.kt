package com.example.composetemplate.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreenEnum

@Composable
fun LoginTextField(
    modifier: Modifier,
    text: String,
    loginScreenEnum: LoginScreenEnum,
    isValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .border(border = BorderStroke(width = 1.dp, if (isValid || text.isEmpty()) Color.Transparent else Color.Red), CircleShape),
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = loginScreenEnum.getPlaceHolder(), color = Color.White)
        },
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.DarkGray,
            focusedContainerColor = Color.DarkGray,
            focusedTextColor = if (isValid) Color.Green else Color.Red,
            unfocusedTextColor = if (isValid) Color.Green else Color.Red
        ),
        singleLine = true,
        keyboardOptions = loginScreenEnum.getKeyboardOptions(),
        visualTransformation = loginScreenEnum.getVisualTransformation(),

        )
    Spacer(modifier = modifier.height(12.dp))
}
