import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.Inject
import platform.Platform

@Composable
fun MainScreen(
    platform: Platform,
    showLeftDrawer: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    shelfViewModel: ShelfViewModel,
    modifier: Modifier,
    parentPaddingValues: PaddingValues,
) {
    val viewModel = remember { Inject.instance<MainScreenViewModel>() }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.selectedPathInfo) {
        viewModel.getSelectedPathInfo()
    }

    Row {
        Box(
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = if (leftDrawerState.isClosed) 0.dp else 0.dp)
                    .background(ApplicationTheme.colors.mainBackgroundColor),
            ) {
                ShelfBoardScreen(
                    platform = platform,
                    viewModel = shelfViewModel,
                    parentPaddingValues = parentPaddingValues
                )
            }

            CustomDockedSearchBar(
                showSearch = showSearch,
                closeSearch = {
                    showSearch.value = false
                },
            )
        }
    }
}
