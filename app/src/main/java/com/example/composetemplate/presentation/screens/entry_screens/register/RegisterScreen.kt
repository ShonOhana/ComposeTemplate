package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.managers.ErrorManager
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.login.loginFields
import com.example.composetemplate.ui.theme.CustomTheme
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.HAVE_ACCOUNT_TEXT
import com.example.composetemplate.utils.Constants.Companion.LOGIN_TEXT
import com.example.composetemplate.utils.Constants.Companion.UNKNOWN_EXCEPTION
import com.example.composetemplate.utils.SuccessCallback
import kotlinx.coroutines.launch

val registerFields = listOf(
    AuthTextFieldsEnum.FULL_NAME,
    AuthTextFieldsEnum.EMAIL,
    AuthTextFieldsEnum.PASSWORD,
    AuthTextFieldsEnum.CONFIRM_PASSWORD
)

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onLoginClicked: () -> Unit,
    isRegisterSucceed: SuccessCallback,
    errorManager: ErrorManager
) {
    val errorMessage by errorManager.errorMessages.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequestList = remember {
        List(registerFields.size) { FocusRequester() }
    }
    // Request focus on the first TextField when the composable is launched
    LaunchedEffect(Unit) {
        focusRequestList.first().requestFocus()
        keyboardController?.show()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.loginScreen),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (index in registerFields.indices) {
                    val loginField = registerFields[index]
                    LoginTextField(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .focusRequester(focusRequestList[index]),
                        text = viewModel.setText(loginField, AuthScreenState.Register),
                        loginScreenEnum = loginField,
                        isValid = viewModel.validateEditText(loginField, AuthScreenState.Register),
                        onValueChange = { newValue ->
                            viewModel.onEvent(loginField, newValue, AuthScreenState.Register)
                        },
                        isLastEditText = index == registerFields.size - 1,
                        isFirstEditText = index == 0,
                        focusRequester = focusRequestList[index],
                        onNextFocusRequest = {
                            if (index < focusRequestList.size - 1) {
                                focusRequestList[index + 1].requestFocus()
                            } else {
                                keyboardController?.hide()
                            }
                        },
                        onDoneClick = {
                            val isValid = viewModel.signupData.isValidRegisterPage
                            if (isValid)
                                viewModel.createEmailPasswordUser(isRegisterSucceed)
                            keyboardController?.hide()
                        }
                    )
                }
            }
            if (errorMessage != null) {
                Text(
                    modifier = modifier
                        .padding(horizontal = 24.dp),
                    text = errorMessage ?: UNKNOWN_EXCEPTION,
                    color = CustomTheme.colors.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }

            LoginScreenButton(
                modifier = modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 24.dp),
                isEnabled = viewModel.signupData.isValidRegisterPage,
                text = LOGIN_TEXT
            ) {
                viewModel.createEmailPasswordUser(isRegisterSucceed)
            }
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { onLoginClicked() },
                text = HAVE_ACCOUNT_TEXT,
                color = CustomTheme.colors.text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
