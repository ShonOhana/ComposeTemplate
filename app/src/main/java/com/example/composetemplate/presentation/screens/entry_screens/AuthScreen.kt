import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.common.FullScreenProgressBar
import com.example.composetemplate.presentation.common.LoginPageHeader
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreen
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.RegisterScreen
import com.example.composetemplate.ui.theme.CustomTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    navigator: Navigator,
    authViewModel: AuthViewModel = koinViewModel(),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr

) {
    val focusManager = LocalFocusManager.current
    var currentScreen by remember { mutableStateOf(AuthScreenState.Login) }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomTheme.colors.loginScreen)
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FullScreenProgressBar(authViewModel.isProgressVisible)
            LoginPageHeader()
            when (currentScreen) {
                AuthScreenState.Login -> LoginScreen(
                    viewModel = authViewModel,
                    onRegisterClicked = { currentScreen = AuthScreenState.Register },
                    isLoginSucceed = { success, exception ->
                        if (success && exception == null)
                            navigator.navigate(MainScreens.Lectures)
                    },
                )

                AuthScreenState.Register -> RegisterScreen(
                    viewModel = authViewModel,
                    onLoginClicked = { currentScreen = AuthScreenState.Login },
                    isRegisterSucceed = { success, exception ->
                        if (success && exception == null)
                            navigator.navigate(MainScreens.Lectures)
                    },
                )
            }

        }
    }
}
