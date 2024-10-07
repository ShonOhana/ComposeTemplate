package com.example.composetemplate.presentation.screens.entry_screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.R
import com.example.composetemplate.presentation.common.BaseNativeDialog
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.registerFields
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_BUTTON_TEXT
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_TITLE
import com.example.composetemplate.utils.Constants.Companion.LOGIN_TEXT
import com.example.composetemplate.utils.SuccessCallback
import kotlinx.coroutines.launch

val loginFields = listOf(
    AuthTextFieldsEnum.EMAIL,
    AuthTextFieldsEnum.PASSWORD
)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onRegisterClicked: () -> Unit,
    isLoginSucceed: SuccessCallback
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val focusRequestList = mutableListOf<FocusRequester>()
    repeat(loginFields.indices.count()) {
        focusRequestList.add(FocusRequester())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.loginScreen)
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ) // Add padding to avoid bottom bar overlap
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween, // Space between first and last items
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (index in loginFields.indices) {
                        val loginField = loginFields[index]
                        LoginTextField(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = viewModel.setText(loginField, AuthScreenState.Login),
                            loginScreenEnum = loginField,
                            isValid = viewModel.validateEditText(loginField, AuthScreenState.Login),
                            onValueChange = { newValue ->
                                viewModel.onEvent(loginField, newValue, AuthScreenState.Login)
                            },
                            isLastEditText = index == loginFields.size - 1,
                            onNextFocusRequest = if (index < focusRequestList.size -1) focusRequestList[index + 1] else focusRequestList[index]
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .graphicsLayer {
                                alpha = if (viewModel.signInData.authError) 1f else 0f
                            },
                        text = Constants.AUTHENTICATION_ERROR_TEXT,
                        color = Color.Red,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    LoginScreenButton(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .padding(horizontal = 24.dp),
                        isEnabled = viewModel.signInData.isValidLoginPage,
                        text = LOGIN_TEXT
                    ) {
                        scope.launch {
                            keyboardController?.hide()
                            viewModel.signInEmailAndPassword(isLoginSucceed)
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .clickable { onRegisterClicked() },
                        text = Constants.REGISTER_TEXT,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable { viewModel.setForgotDialogVisibility(true) },
                    text = "Forgot Password",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
    ForgotPasswordDialog(viewModel)
}

@Composable
fun ForgotPasswordDialog(
    viewModel: AuthViewModel
) {
    if (viewModel.isForgotDialogVisible) {
        BaseNativeDialog(
            modifier = Modifier,
            onDismissRequest = { viewModel.setForgotDialogVisibility(false) }) {
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
                        viewModel.resetPassword(viewModel.forgotPasswordMail)
                        viewModel.setForgotDialogVisibility(false)
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

