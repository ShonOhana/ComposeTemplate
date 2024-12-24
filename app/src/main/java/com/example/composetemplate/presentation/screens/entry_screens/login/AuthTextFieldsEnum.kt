package com.example.composetemplate.presentation.screens.entry_screens.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.composetemplate.managers.StringsKeyManager.FORGOT_PASSWORD_TEXT
import com.example.composetemplate.managers.StringsKeyManager.LOGIN_CONFIRM_PASSWORD_PLACEHOLDER
import com.example.composetemplate.managers.StringsKeyManager.LOGIN_EMAIL_PLACEHOLDER
import com.example.composetemplate.managers.StringsKeyManager.LOGIN_FULL_NAME_PLACEHOLDER
import com.example.composetemplate.managers.StringsKeyManager.LOGIN_PASSWORD_PLACEHOLDER
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.StringProvider

enum class AuthScreenState {
    Login,
    Register
}

enum class AuthTextFieldsEnum {
    EMAIL, PASSWORD, FULL_NAME, CONFIRM_PASSWORD, FORGOT_PASSWORD ;

    fun getPlaceHolder(): String{
        return when(this) {
            EMAIL -> StringProvider.getString(LOGIN_EMAIL_PLACEHOLDER)
            PASSWORD -> StringProvider.getString(LOGIN_PASSWORD_PLACEHOLDER)
            FULL_NAME -> StringProvider.getString(LOGIN_FULL_NAME_PLACEHOLDER)
            CONFIRM_PASSWORD -> StringProvider.getString(LOGIN_CONFIRM_PASSWORD_PLACEHOLDER)
            FORGOT_PASSWORD -> StringProvider.getString(FORGOT_PASSWORD_TEXT)
        }
    }

    fun getKeyboardOptions(isLastEditText: Boolean): KeyboardOptions {
        return when (this) {
            FORGOT_PASSWORD,
            EMAIL -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            CONFIRM_PASSWORD,
            PASSWORD -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = if (isLastEditText) ImeAction.Done else ImeAction.Next
            )
            FULL_NAME -> KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            )
        }
    }

    fun getVisualTransformation(): VisualTransformation {
        return when(this) {
            FORGOT_PASSWORD,
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