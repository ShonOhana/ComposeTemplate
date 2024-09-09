package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.ui.theme.LoginScreenColor
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.LoginText

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
    onLoginClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LoginScreenColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(LoginScreenColor),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(registerFields.size) { index ->
                val loginField = registerFields[index]
                LoginTextField(
                    modifier = Modifier,
                    text = viewModel.setText(loginField, AuthScreenState.Register),
                    loginScreenEnum = loginField,
                    isValid = viewModel.validateEditText(loginField, AuthScreenState.Register),
                    onValueChange = { newValue ->
                        viewModel.onEvent(loginField, newValue, AuthScreenState.Register)
                    },
                    isLastEditText = index == registerFields.size - 1
                )
            }
            if (viewModel.signupData.authError) {
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 24.dp),
                        text = Constants.AuthenticationErrorText,
                        color = Color.Red,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                LoginScreenButton(
                    modifier = modifier.padding(top = 12.dp),
                    isEnabled = viewModel.signupData.isValidRegisterPage,
                    text = LoginText
                ) {
                    viewModel.createEmailPasswordUser()
                }
            }
            item {
                Text(
                    modifier = modifier
                        .padding(top = 12.dp)
                        .clickable {
                            viewModel.logOut()
                            onLoginClicked()
                        },
                    text = "Already have an account? Login! ",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
