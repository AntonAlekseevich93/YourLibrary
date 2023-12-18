import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import di.Inject


@Composable
fun ShelfBoard() {
    val shelfViewModel = remember { Inject.instance<ShelfViewModel>() }
    val kanbanList by shelfViewModel.kanbanList.collectAsState()
    val defaultKanbanCardColor = ApplicationTheme.colors.cardBackgroundDark
    val kanbanBackgroundColor = remember { mutableStateOf(defaultKanbanCardColor) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ApplicationTheme.colors.mainBackgroundColor
    ) {


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StuffListUI(openCard: () -> Unit) {
    val viewModel = remember { Inject.instance<ShelfViewModel>() }
    val uiState = viewModel.uiState.collectAsState()


    //todo row можно будет потом удалить


}