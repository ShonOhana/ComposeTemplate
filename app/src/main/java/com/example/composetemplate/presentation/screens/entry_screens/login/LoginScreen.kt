package com.example.composetemplate.presentation.screens.entry_screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composetemplate.presentation.common.LoginScreenButton
import com.example.composetemplate.presentation.common.LoginTextField
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.ui.theme.LoginScreenColor
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.Constants.Companion.LoginText

val loginFields = listOf(
    AuthTextFieldsEnum.EMAIL,
    AuthTextFieldsEnum.PASSWORD
)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    onRegisterClicked: () -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LoginScreenColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(loginFields.size) { index ->
            val loginField = loginFields[index]
            LoginTextField(
                modifier = Modifier,
                text = viewModel.setText(loginField, LoginScreenStateManagement.Login),
                loginScreenEnum = loginField,
                isValid = viewModel.validateEditText(loginField, LoginScreenStateManagement.Login),
                onValueChange = { newValue ->
                    viewModel.onEvent(loginField, newValue, LoginScreenStateManagement.Login)
                },
                isLastEditText = index == loginFields.size - 1
            )
        }
        item {
            Text(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .graphicsLayer { alpha = if (viewModel.signInData.authError) 1f else 0f },
                text = Constants.AuthenticationErrorText,
                color = Color.Red,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
        item {
            LoginScreenButton(
                modifier = modifier.padding(top = 12.dp),
                isEnabled = viewModel.signInData.isValidLoginPage,
                text = LoginText
            ) {
                viewModel.signInEmailAndPassword()
            }
        }
        item {
            Text(
                modifier = modifier
                    .padding(top = 12.dp)
                    .clickable { onRegisterClicked() },
                text = Constants.RegisterText,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
