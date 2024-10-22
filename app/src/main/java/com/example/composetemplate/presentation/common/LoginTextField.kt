package com.example.composetemplate.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.ui.theme.CustomTheme

@Composable
fun LoginTextField(
    modifier: Modifier,
    text: String,
    loginScreenEnum: AuthTextFieldsEnum,
    isValid: Boolean,
    onValueChange: (String) -> Unit,
    isLastEditText: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester ? = null,
    onNextFocusRequest: FocusRequester? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .focusRequester(focusRequester ?: FocusRequester())
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    if (text.isEmpty() || isFocused) Color.Transparent
                    else {
                        if (!isValid) Color.Red else Color.Transparent
                    }
                ), CustomTheme.shapes.roundedTextField
            ),
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = loginScreenEnum.getPlaceHolder(), color = CustomTheme.colors.text)
        },
        leadingIcon = leadingIcon,
        shape = CustomTheme.shapes.roundedTextField,
        colors = TextFieldDefaults.colors(
            cursorColor = CustomTheme.colors.cursor,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = CustomTheme.colors.error,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = CustomTheme.colors.authEditTextContainer,
            focusedContainerColor = CustomTheme.colors.authEditTextContainer,
            focusedTextColor = CustomTheme.colors.loginEnable,
            unfocusedTextColor = if (isValid) CustomTheme.colors.loginEnable else CustomTheme.colors.error
        ),
        singleLine = true,
        keyboardOptions = loginScreenEnum.getKeyboardOptions(isLastEditText),
        keyboardActions = KeyboardActions(
            onNext = {
                onNextFocusRequest?.requestFocus() // Move to next TextField
            },
            onDone = {
                keyboardController?.hide()
            }
        ),
        visualTransformation = loginScreenEnum.getVisualTransformation(),

        )
    // Automatically request focus and show the keyboard
//    LaunchedEffect(Unit) {
//        focusRequester?.requestFocus()
//        keyboardController?.show()
//    }
    Spacer(modifier = modifier.height(12.dp))
}
