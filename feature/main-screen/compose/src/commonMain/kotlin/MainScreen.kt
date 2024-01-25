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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import kotlinx.coroutines.launch
import menu_bar.LeftMenuBar
import models.MainScreenUiState
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.contents.LeftDrawerBooksContent
import platform.Platform
import platform.isDesktop
import sub_app_bar.SubAppBar
import tooltip_area.TooltipItem

@Composable
fun MainScreen(
    uiState: MainScreenUiState,
    platform: Platform,
    showLeftDrawer: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    viewModel: MainScreenViewModel,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    openBookListener: (painterSelectedBookInCache: Resource<Painter>?, bookId: String) -> Unit,
    createBookListener: () -> Unit,
    selectAnotherVaultListener: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedPathInfo) {
        viewModel.getSelectedPathInfo()
    }

    Row {
        if (platform.isDesktop()) {
            LeftMenuBar(
                searchListener = {
                    showSearch.value = true
                },
                open = {
                },
                tooltipCallback = tooltipCallback,
                createBookListener = createBookListener,
                selectAnotherVaultListener = selectAnotherVaultListener,
            )
        }

        PlatformNavigationDrawer(
            platform = platform,
            leftDrawerContent = {
                PlatformLeftDrawerContent(
                    title = uiState.selectedPathInfo.value.libraryName,
                    platform = platform,
                    closeSidebarListener = {
                        scope.launch {
                            if (!showLeftDrawer.value) {
                                showLeftDrawer.value = true
                                leftDrawerState.open()
                            } else {
                                showLeftDrawer.value = false
                                leftDrawerState.close()
                            }
                        }
                    },
                    tooltipCallback = tooltipCallback,
                    content = {
                        LeftDrawerBooksContent(
                            booksInfoUiState = uiState.booksInfoUiState,
                            tooltipCallback = tooltipCallback,
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
                    SubAppBar(
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                        projectName = "Книжная полка",
                        selectedViewsTypes = uiState.viewsTypes.selectedViewTypes,
                        isCheckedTypes = uiState.viewsTypes.checkedViewTypes,
                        isOpenedType = uiState.viewsTypes.openedViewType.value,
                        openViewType = viewModel::openViewType,
                        isOpenedSidebar = showLeftDrawer,
                        switchViewTypesListener = viewModel::switchViewTypesListener,
                        closeViewsTypeDropdown = viewModel::changeViewsTypes,
                        openSidebarListener = {
                            scope.launch {
                                if (!showLeftDrawer.value) {
                                    showLeftDrawer.value = true
                                    leftDrawerState.open()
                                } else {
                                    showLeftDrawer.value = false
                                    leftDrawerState.close()
                                }
                            }
                        },
                        tooltipCallback = tooltipCallback,
                        homeButtonListener = createBookListener
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
