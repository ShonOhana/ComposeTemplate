package com.example.composetemplate.presentation.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun BasicTextField(
    value: String,
    placeholderModifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    singleLine: Boolean = true,
    placeholderText: String? = null,
    icon: ImageVector? = null,
    shape: Shape? = TextFieldDefaults.shape,
    type: TextFieldType,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = TextStyle(fontSize = 20.sp),
    onValueChange: (String) -> Unit
) {

    when (type) {
        TextFieldType.ICON_START -> TODO()
        TextFieldType.ICON_END -> TODO()
        TextFieldType.TEXT_ONLY ->
            TextOnlyTextField(
                value = value,
                placeholderModifier = placeholderModifier,
                placeholderText = placeholderText,
                shape = shape,
                textFieldModifier = textFieldModifier,
                onValueChange = onValueChange,
                singleLine = singleLine,
                colors = colors,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                keyboardActions = keyboardActions,
                textStyle = textStyle
            )
    }
}

@Composable
private fun TextOnlyTextField(
    value: String,
    placeholderModifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    singleLine: Boolean = true,
    placeholderText: String? = null,
    shape: Shape? = TextFieldDefaults.shape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textStyle: TextStyle = TextStyle(fontSize = 20.sp),
    onValueChange: (String) -> Unit
) {
    if (shape != null) {
        TextField(
            modifier = textFieldModifier,
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            shape = shape,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            colors = colors,
            textStyle = textStyle,
            placeholder = {
                if (placeholderText != null) {
                    PlaceHolder(placeholderText, placeholderModifier)
                }
            }
        )
    }
}

@Composable
private fun PlaceHolder(
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    Text(text = placeholderText, modifier = modifier)
}

enum class TextFieldType {
    ICON_START, ICON_END, TEXT_ONLY
}