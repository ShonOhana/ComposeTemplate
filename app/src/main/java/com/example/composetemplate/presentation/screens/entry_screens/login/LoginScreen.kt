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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.composetemplate.managers.ErrorManager
import com.example.composetemplate.presentation.common.BaseNativeDialog
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.dialogs.ForgotPasswordDialog
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.registerFields
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_BUTTON_TEXT
import com.example.composetemplate.utils.Constants.Companion.FORGOT_PASSWORD_TITLE
import com.example.composetemplate.utils.Constants.Companion.LOGIN_TEXT
import com.example.composetemplate.utils.Constants.Companion.UNKNOWN_EXCEPTION
import com.example.composetemplate.utils.SuccessCallback
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

val loginFields = listOf(
    AuthTextFieldsEnum.EMAIL,
    AuthTextFieldsEnum.PASSWORD
)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onRegisterClicked: () -> Unit,
    isLoginSucceed: SuccessCallback,
    errorManager: ErrorManager
) {
    val errorMessage by errorManager.errorMessages.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val focusRequestList = remember {
        List(loginFields.size) { FocusRequester() }
    }
    // Request focus on the first TextField when the composable is launched
    LaunchedEffect(Unit) {
        focusRequestList.first().requestFocus()
        keyboardController?.show()
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
                            modifier = Modifier.padding(horizontal = 24.dp)
                                .focusRequester(focusRequestList[index]),
                            text = viewModel.setText(loginField, AuthScreenState.Login),
                            loginScreenEnum = loginField,
                            isValid = viewModel.validateEditText(loginField, AuthScreenState.Login),
                            onValueChange = { newValue ->
                                viewModel.onEvent(loginField, newValue, AuthScreenState.Login)
                            },
                            isLastEditText = index == loginFields.size - 1,
                            isFirstEditText = index == 0,
                            onNextFocusRequest = {
                                if (index < focusRequestList.size - 1) {
                                    focusRequestList[index + 1].requestFocus()
                                } else {
                                    keyboardController?.hide()
                                }
                            },
                            onDoneClick = {
                                val isValid = viewModel.signInData.isValidLoginPage
                                if (isValid)
                                    viewModel.signInEmailAndPassword(isLoginSucceed)
                                keyboardController?.hide()
                            }
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .graphicsLayer {
                                alpha = if (errorMessage != null) 1f else 0f
                            },
                        text = errorMessage ?: UNKNOWN_EXCEPTION,
                        color = CustomTheme.colors.error,
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
                        color = CustomTheme.colors.text,
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
                    color = CustomTheme.colors.text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
    ForgotPasswordDialog(viewModel)
}

