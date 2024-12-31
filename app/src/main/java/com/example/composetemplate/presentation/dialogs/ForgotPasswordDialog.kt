package com.example.composetemplate.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.R
import com.example.composetemplate.presentation.common.BaseNativeDialog
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_BUTTON_TEXT
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_TITLE
import com.example.composetemplate.utils.extensions.isValidEmail
import kotlinx.coroutines.launch


@Composable
fun ForgotPasswordDialog(
    viewModel: AuthViewModel
) {
    if (viewModel.isDialogVisible) {
        val scope = rememberCoroutineScope()
        BaseNativeDialog(
            modifier = Modifier,
            onDismissRequest = { viewModel.setDialogVisibility(false) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Center title
            ) {
                // Title in the middle
                Text(
                    text = FORGOT_PASSWORD_TITLE,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center, // Center the text inside the Text composable
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                LoginTextField(
                    modifier = Modifier,
                    text = viewModel.setText(
                        AuthTextFieldsEnum.FORGOT_PASSWORD,
                        AuthScreenState.Login
                    ),
                    loginScreenEnum = AuthTextFieldsEnum.FORGOT_PASSWORD,
                    isValid = viewModel.validateEditText(
                        AuthTextFieldsEnum.FORGOT_PASSWORD,
                        AuthScreenState.Login
                    ),
                    onValueChange = { newValue ->
                        viewModel.onEvent(
                            AuthTextFieldsEnum.FORGOT_PASSWORD,
                            newValue,
                            AuthScreenState.Login
                        )
                    },
                    isLastEditText = true,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.email_icon),
                            contentDescription = "Email Icon",
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Button below the TextField
                LoginScreenButton(
                    onClick = {
                        if (!viewModel.forgotPasswordMail.isValidEmail()) {
                            return@LoginScreenButton
                        }
                        scope.launch {
                            viewModel.setDialogVisibility(false)
                            viewModel.resetPassword(viewModel.forgotPasswordMail)
                        }
                    },
                    isEnabled = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = FORGOT_PASSWORD_BUTTON_TEXT
                )
            }
        }
    }
}