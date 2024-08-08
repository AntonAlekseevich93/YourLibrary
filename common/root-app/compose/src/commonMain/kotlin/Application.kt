import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import authors_screen.AuthorsScreen
import book_info.BookInfoScreen
import bottom_app_bar.CustomBottomBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import join_authors_screen.JoinAuthorsScreen
import kotlinx.coroutines.launch
import main_app_bar.MainAppBar
import main_models.TooltipItem
import menu_bar.LeftMenuBar
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.contents.LeftDrawerBooksContent
import platform.Platform
import platform.isDesktop
import platform.isMobile
import selecting_project.CreationAndSelectionProjectFolderScreen
import tooltip_area.ShowTooltip

@Composable
fun Application(
    platform: Platform,
    isKeyboardShown: State<Boolean> = mutableStateOf(false),
    navigator: Navigator,
    desktopTooltip: MutableState<TooltipItem>? = null,
    platformDisplayHeight: Dp? = null,
) {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    val shelfViewModel = remember { Inject.instance<ShelfViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showSearch = remember { mutableStateOf(false) }
    val dbPathExist = remember { mutableStateOf(viewModel.isDbPathIsExist(platform)) }
    val scope = rememberCoroutineScope()
    val canShowLeftBar = remember { mutableStateOf(true) }
    val canShowMainButton = remember { mutableStateOf(true) }
    val hazeBlurState = remember { HazeState() }
    val showSearchAppBarTextField = remember { mutableStateOf(false) }
    val openedRoute = remember { mutableStateOf(Routes.main_route) }

    AppTheme {
        navigator.currentEntry.collectAsState(null).value?.route?.route?.let { currentRoute ->
            openedRoute.value = currentRoute
            if (!uiState.fullScreenBookInfo.value && platform.isMobile() && currentRoute == Routes.main_route) {
                uiState.fullScreenBookInfo.value = true
            }
            canShowLeftBar.value = currentRoute != Routes.vault_route
            canShowMainButton.value = currentRoute != Routes.main_route
        }

        Box(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)) {
            Row {
                AnimatedVisibility(platform.isDesktop() && canShowLeftBar.value) {
                    viewModel.LeftMenuBar(
                        open = {
                        },
                    )
                }
                PlatformNavigationDrawer(
                    platform = platform,
                    leftDrawerContent = {
                        Row {
                            viewModel.PlatformLeftDrawerContent(
                                title = uiState.selectedPathInfo.value.libraryName,
                                platform = platform,
                                canShowHomeButton = canShowMainButton,
                                content = {
                                    viewModel.LeftDrawerBooksContent(booksInfoUiState = uiState.booksInfoUiState)
                                }
                            )
                            Divider(
                                modifier = Modifier.fillMaxHeight().width(1.dp),
                                thickness = 1.dp,
                                color = ApplicationTheme.colors.divider
                            )
                        }
                    },
                    leftDrawerState = leftDrawerState,
                    showLeftDrawer = uiState.showLeftDrawerState
                ) {
                    Scaffold(
                        containerColor = ApplicationTheme.colors.mainBackgroundColor,
                        topBar = {
                            if (openedRoute.value == Routes.main_route) {
                                viewModel.MainAppBar(
                                    hazeBlurState = hazeBlurState,
                                    searchedBooks = uiState.searchedBooks,
                                    platformDisplayHeight = platformDisplayHeight,
                                    showSearchAppBarTextField = showSearchAppBarTextField,
                                    changeVisibilitySearchAppBarTextField = {
                                        showSearchAppBarTextField.value =
                                            !showSearchAppBarTextField.value
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            if (platform.isMobile()) {
                                viewModel.CustomBottomBar(hazeState = hazeBlurState)
                            }
                        },
                        modifier = Modifier
                    ) { paddingValues ->
                        NavHost(
                            navigator = navigator,
                            initialRoute = if (platform.isDesktop() && !dbPathExist.value) Routes.vault_route else Routes.main_route,
                            navTransition = NavTransition(
                                createTransition = fadeIn(animationSpec = tween(durationMillis = 1)),
                                destroyTransition = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                            ),
                        ) {
                            scene(
                                route = Routes.main_route,
                                navTransition = NavTransition(
                                    createTransition = fadeIn(tween(1)),
                                    destroyTransition = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                                )
                            ) {
                                MainScreen(
                                    platform = platform,
                                    showLeftDrawer = uiState.showLeftDrawerState,
                                    showSearch = showSearch,
                                    leftDrawerState = leftDrawerState,
                                    shelfViewModel = shelfViewModel,
                                    modifier = Modifier.pointerInput(showSearchAppBarTextField.value) {
                                        if (showSearchAppBarTextField.value) {
                                            showSearchAppBarTextField.value = false
                                        }
                                    }.haze(
                                        hazeBlurState,
                                        HazeStyle(
                                            tint = Color.Black.copy(alpha = .2f),
                                            blurRadius = 30.dp,
                                        )
                                    ),
                                    parentPaddingValues = paddingValues
                                )
                            }

                            dialog(route = Routes.book_info_route) {
                                BookInfoScreen(
                                    bookItemId = uiState.selectedBookId.value,
                                    bookShortVo = null
                                )
                            }

                            scene(route = Routes.book_creator_route) {
                                BookCreatorScreen(
                                    platform = platform,
                                    fullScreenBookCreator = mutableStateOf(false),
                                    isKeyboardShown = isKeyboardShown,
                                    showRightDrawer = uiState.showRightDrawerState,
                                    hazeState = hazeBlurState
                                )
                            }

                            scene(
                                route = Routes.vault_route,
                                navTransition = NavTransition(
                                    createTransition = fadeIn(tween(1)),
                                    destroyTransition = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                                )
                            ) {
                                viewModel.CreationAndSelectionProjectFolderScreen(
                                    pathInfoList = uiState.pathInfoList,
                                )
                            }

                            scene(
                                route = Routes.authors_screen_route,
                                navTransition = NavTransition(
                                    createTransition = expandHorizontally(),
                                    destroyTransition = slideOutHorizontally(tween(100))
                                )
                            ) {
                                AuthorsScreen(
                                    showLeftDrawer = uiState.showLeftDrawerState
                                )
                            }

                            dialog(route = Routes.join_authors_screen_route) {
                                JoinAuthorsScreen(
                                    showLeftDrawer = uiState.showLeftDrawerState,
                                )
                            }

                            dialog(route = Routes.settings_screen_route) {
                                SettingsScreen(platform)
                            }

                            scene(
                                route = Routes.profile_screen_route,
                                navTransition = NavTransition(
                                    createTransition = expandHorizontally(),
                                    destroyTransition = slideOutHorizontally(tween(100))
                                )
                            ) {
                                ProfileScreen(
                                    showLeftDrawer = uiState.showLeftDrawerState
                                )
                            }

                            scene(
                                route = Routes.admin_screen_route,
                                navTransition = NavTransition(
                                    createTransition = expandHorizontally(),
                                    destroyTransition = slideOutHorizontally(tween(100))
                                )
                            ) {
                                AdminPanelScreen(
                                    showLeftDrawer = uiState.showLeftDrawerState
                                )
                            }
                        }
                    }
                }
            }
            if (platform.isDesktop() && desktopTooltip?.value?.showTooltip == true) {
                ShowTooltip(desktopTooltip.value)
            }
        }
    }

    uiState.closeLeftDrawerEvent.value = {
        scope.launch {
            leftDrawerState.close()
        }
    }

    uiState.openLeftDrawerEvent.value = {
        scope.launch {
            leftDrawerState.open()
        }
    }

    uiState.closeRightDrawerEvent.value = {
        scope.launch {
            rightDrawerState.close()
        }
    }

    uiState.openRightDrawerEvent.value = {
        scope.launch {
            rightDrawerState.open()
        }
    }
}