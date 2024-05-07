package com.example.composetemplate.presentation.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetemplate.ui.theme.ComposeTemplateTheme
import com.example.composetemplate.ui.theme.LoginScreenColor
import com.example.composetemplate.presentation.common.LoginPageHeader
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.login.LoginEvent
import com.example.composetemplate.presentation.login.LoginScreenEnum
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterPagePageClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LoginScreenColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LoginPageHeader()

        LoginTextField(
            modifier,
            text = viewModel.loginState.fullName,
            loginScreenEnum = LoginScreenEnum.FULL_NAME,
            isValid = viewModel.loginState.isFullNameValid,
            onValueChange = {
                viewModel.onEvent(LoginEvent.UpdateFullName(it))
            }
        )

        LoginTextField(
            modifier,
            text = viewModel.loginState.email,
            loginScreenEnum = LoginScreenEnum.EMAIL,
            isValid = viewModel.loginState.isEmailValid,
            onValueChange = {
                viewModel.onEvent(LoginEvent.UpdateEmail(it))
            }
        )

        LoginTextField(
            modifier,
            text = viewModel.loginState.password,
            loginScreenEnum = LoginScreenEnum.PASSWORD,
            isValid = viewModel.loginState.isPasswordValid,
            onValueChange = {
                viewModel.onEvent(LoginEvent.UpdatePassword(it))
            }
        )


        LoginTextField(
            modifier,
            text = viewModel.loginState.confirmPassword,
            loginScreenEnum = LoginScreenEnum.CONFIRM_PASSWORD,
            isValid = viewModel.loginState.isConfirmPasswordValid,
            onValueChange = {
                viewModel.onEvent(LoginEvent.UpdateConfirmPassword(it))
            }
        )


        LoginScreenButton(modifier = modifier.padding(top = 12.dp), isEnabled = viewModel.loginState.isValidRegisterPage, text = "Login") {
            //todo: onClick
        }

        Text(
            modifier = modifier
                .padding(top = 12.dp)
                .clickable { onRegisterPagePageClick() },
            text = "Already have an account? Login! ",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge
        )

    }
}


@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterScreenPrev() {
    ComposeTemplateTheme {
        RegisterScreen(viewModel = RegisterViewModel()) {}
    }
}