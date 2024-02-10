import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.Inject
import toolbar.CommonToolbar

@Composable
fun AuthorsScreen(
    showLeftDrawer: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<AuthorsViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor)
    ) {
        viewModel.CommonToolbar(showLeftDrawer){
           Spacer(Modifier.weight(1f))
                Text(
                    text = Strings.autors,
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                )

        }
        viewModel.AuthorsScreenContent(uiState)
    }

}