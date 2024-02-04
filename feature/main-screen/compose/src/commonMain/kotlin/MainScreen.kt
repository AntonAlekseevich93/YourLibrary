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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import menu_bar.LeftMenuBar
import models.MainScreenUiState
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.contents.LeftDrawerBooksContent
import platform.Platform
import platform.isDesktop
import sub_app_bar.SubAppBar

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    platform: Platform,
    showLeftDrawer: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    viewModel: MainScreenViewModel,
    openBookListener: (painterSelectedBookInCache: Resource<Painter>?, bookId: String) -> Unit,
) {
    LaunchedEffect(uiState.selectedPathInfo) {
        viewModel.getSelectedPathInfo()
    }

    Row {
        if (platform.isDesktop()) {
            viewModel.LeftMenuBar(
                open = {
                },
            )
        }

        PlatformNavigationDrawer(
            platform = platform,
            leftDrawerContent = {
                viewModel.PlatformLeftDrawerContent(
                    title = uiState.selectedPathInfo.value.libraryName,
                    platform = platform,
                    content = {
                        viewModel.LeftDrawerBooksContent(
                            booksInfoUiState = uiState.booksInfoUiState,
                            openBookListener = { openBookListener.invoke(null, it) }
                        )
                    }
                )
            },
            leftDrawerState = leftDrawerState,
            showLeftDrawer = showLeftDrawer
        ) {
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
                    ShelfBoardScreen(
                        platform = platform,
                        openBookListener = openBookListener,
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
}
