import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.Inject

@Composable
fun AuthorsScreen(
) {
    val viewModel = remember { Inject.instance<AuthorsViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollableState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollableState)
    ) {
        AuthorsScreenContent(uiState)
    }

}