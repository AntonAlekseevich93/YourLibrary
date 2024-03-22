import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.Inject

@Composable
fun ProfileScreen(
    showLeftDrawer: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Column( modifier = Modifier
        .fillMaxSize()
        .background(ApplicationTheme.colors.mainBackgroundColor)) {

    }
}