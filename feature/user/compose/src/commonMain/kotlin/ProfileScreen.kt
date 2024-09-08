import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import auth.AuthScreen
import auth.VerifiedScreen
import di.Inject
import profile.ProfileContent

@Composable
fun ProfileScreen(
) {
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor)
    ) {
        if (uiState.isAuthorized.value) {
            if (uiState.userInfo.value.isVerified) {
                viewModel.ProfileContent(uiState.userInfo)
            } else {
                viewModel.VerifiedScreen()
            }
        } else {
            viewModel.AuthScreen(uiState.isSignUnState)
        }
    }
}