import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import sub_app_bar.SubAppBar

@Composable
fun MainScreen(
    platform: Platform,
    showLeftDrawer: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    shelfViewModel: ShelfViewModel,
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
                modifier = Modifier
                    .padding(start = if (leftDrawerState.isClosed) 0.dp else 0.dp)
                    .fillMaxSize()
                    .background(ApplicationTheme.colors.mainBackgroundColor),
            ) {
                viewModel.SubAppBar(
                    modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                    projectName = "Книжная полка",
                    selectedViewsTypes = uiState.viewsTypes.selectedViewTypes,
                    isCheckedTypes = uiState.viewsTypes.checkedViewTypes,
                    isOpenedType = uiState.viewsTypes.openedViewType.value,
                    openViewType = viewModel::openViewType,
                    isOpenedSidebar = showLeftDrawer,
                    switchViewTypesListener = viewModel::switchViewTypesListener,
                    closeViewsTypeDropdown = viewModel::changeViewsTypes,
                    homeButtonListener = {}
                )
                ShelfBoardScreen(platform = platform, viewModel = shelfViewModel)
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
