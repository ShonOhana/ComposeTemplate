package com.example.composetemplate.presentation.screens.entry_screens.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.composetemplate.utils.Constants

enum class AuthScreenState {
    Login,
    Register
}

enum class AuthTextFieldsEnum {
    EMAIL, PASSWORD, FULL_NAME, CONFIRM_PASSWORD ;

    fun getPlaceHolder(): String{
        return when(this) {
            EMAIL -> Constants.EMAIL_TEXT
            PASSWORD -> Constants.PASSWORD_TEXT
            FULL_NAME -> Constants.FULL_NAME_TEXT
            CONFIRM_PASSWORD -> Constants.CONFIRM_PASSWORD_TEXT
        }
    }

    fun getKeyboardOptions(isLastEditText: Boolean): KeyboardOptions {
        return when(this) {
            EMAIL -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            CONFIRM_PASSWORD,
            PASSWORD -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = if (isLastEditText) ImeAction.Done else ImeAction.Next)
            FULL_NAME -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text,imeAction = ImeAction.Next)
        }
    }

    fun getVisualTransformation(): VisualTransformation {
        return when(this) {
            FULL_NAME,
            EMAIL -> VisualTransformation.None
            CONFIRM_PASSWORD,
            PASSWORD ->  PasswordVisualTransformation()
        }
    }
    // Custom VisualTransformation for Password field
    private class PasswordVisualTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            return TransformedText(AnnotatedString("*".repeat(text.text.length)), offsetMapping = OffsetMapping.Identity)
        }
    }

}