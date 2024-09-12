import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composetemplate.navigation.MainScreens
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.common.FullScreenProgressBar
import com.example.composetemplate.presentation.common.LoginPageHeader
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginScreen
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.register.AuthViewModel
import com.example.composetemplate.presentation.screens.entry_screens.register.RegisterScreen
import com.example.composetemplate.ui.theme.LoginScreenColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    navigator: Navigator,
    authViewModel: AuthViewModel = koinViewModel()
){
    var currentScreen by remember { mutableStateOf(AuthScreenState.Login) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginScreenColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FullScreenProgressBar(authViewModel.isProgressVisible)
        LoginPageHeader()
        when (currentScreen) {
            AuthScreenState.Login -> LoginScreen(
                viewModel = authViewModel,
                onRegisterClicked = { currentScreen = AuthScreenState.Register },
                isLoginSucceed = { succeess, exception ->
                    if (succeess && exception == null)
                        navigator.navigate(MainScreens.Home)
                }
            )
            AuthScreenState.Register -> RegisterScreen(
                viewModel = authViewModel,
                onLoginClicked = { currentScreen = AuthScreenState.Login },
                isRegisterSucceed = { succeess, exception ->
                    if (succeess && exception == null)
                        navigator.navigate(MainScreens.Home)
                }
            )
        }
    }
}
