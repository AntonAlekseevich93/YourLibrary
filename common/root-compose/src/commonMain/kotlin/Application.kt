import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import di.Inject
import io.kamel.core.Resource
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import platform.Platform
import platform.isDesktop
import platform.isMobile
import screens.selecting_project.CreationAndSelectionProjectFolderScreen
import tooltip_area.ShowTooltip
import tooltip_area.TooltipItem

@Composable
fun Application(platform: Platform, restartWindow: (() -> Unit)? = null) {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    val settingsViewModel = remember { Inject.instance<SettingsViewModel>() }
    val mainScreenViewModel = remember { Inject.instance<MainScreenViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val mainScreenUiState by mainScreenViewModel.uiState.collectAsState()
    val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showLeftDrawer = remember { mutableStateOf(false) }
    val showRightDrawer = remember { mutableStateOf(false) }
    val showSearch = remember { mutableStateOf(false) }
    val fullScreenBookInfo = remember { mutableStateOf(false) }
    val tooltip = remember { mutableStateOf(TooltipItem()) }
    val painterSelectedBookInCache: MutableState<Resource<Painter>?> = mutableStateOf(null)
    val selectedBookId: MutableState<String> = mutableStateOf("")
    val dbPathExist = remember { mutableStateOf(viewModel.isDbPathIsExist(platform)) }
    val scope = rememberCoroutineScope()

    AppTheme {
        PreComposeApp {
            val navigator = rememberNavigator()
            navigator.currentEntry.collectAsState(null).value?.route?.route?.let { currentRoute ->
                if (!fullScreenBookInfo.value && platform.isMobile() && currentRoute == Routes.main_route) {
                    fullScreenBookInfo.value = true
                }
            }

            Box(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)) {
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
                            uiState = mainScreenUiState,
                            platform = platform,
                            showLeftDrawer = showLeftDrawer,
                            showSearch = showSearch,
                            leftDrawerState = leftDrawerState,
                            viewModel = mainScreenViewModel,
                            openBookListener = { painter, bookId ->
                                painterSelectedBookInCache.value = painter
                                selectedBookId.value = bookId
                                navigator.navigate(
                                    route = Routes.book_info_route,
                                    options = NavOptions(popUpTo = PopUpTo.Prev),
                                )
                            },
                            tooltipCallback = {
                                tooltip.value = it
                            },
                            createBookListener = {
                                navigator.navigate(
                                    route = Routes.book_creator_route,
                                    options = NavOptions(launchSingleTop = false),
                                )
                            },
                            selectAnotherVaultListener = {
                                tooltip.value.showTooltip = false
                                navigator.navigate(route = Routes.vault_route)
                            }
                        )
                    }

                    dialog(route = Routes.book_info_route) {
                        BookScreen(
                            platform = platform,
                            bookItemId = selectedBookId.value,
                            showLeftDrawer = showLeftDrawer,
                            showRightDrawer = showRightDrawer,
                            showSearch = showSearch,
                            leftDrawerState = leftDrawerState,
                            rightDrawerState = rightDrawerState,
                            fullScreenBookInfo = fullScreenBookInfo,
                            painterInCache = painterSelectedBookInCache.value,
                            tooltipCallback = {
                                tooltip.value = it
                            },
                            onClose = {
                                fullScreenBookInfo.value = false
                                tooltip.value.showTooltip = false
                                navigator.goBack()
                            },
                            createBookListener = {
                                navigator.navigate(
                                    route = Routes.book_creator_route,
                                    options = NavOptions(popUpTo = PopUpTo(Routes.main_route)),
                                )
                            },
                            selectAnotherVaultListener = {
                                tooltip.value.showTooltip = false
                                navigator.popBackStack() //todo здесь происходит мигание анимации, но без этого баг. Подумать
                                navigator.navigate(route = Routes.vault_route)
                            },
                            changeReadingStatusListener = { oldStatusId, bookId ->
                                mainScreenViewModel.changedReadingStatus(oldStatusId, bookId)
                            },
                            bookItemWasChangedListener = { oldItem, newItem ->
                                mainScreenViewModel.checkIfNeedUpdateBookItem(oldItem, newItem)
                            }
                        )
                    }

                    dialog(route = Routes.book_creator_route) {
                        BookCreatorScreen(
                            platform = platform,
                            fullScreenBookCreator = mutableStateOf(false),
                            showRightDrawer = showRightDrawer,
                            tooltipCallback = { tooltip.value = it },
                            closeBookCreatorListener = {
                                navigator.goBack()
                            }
                        )
                    }

                    scene(
                        route = Routes.vault_route,
                        navTransition = NavTransition(
                            createTransition = fadeIn(tween(1)),
                            destroyTransition = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                        )
                    ) {
                        CreationAndSelectionProjectFolderScreen(
                            pathInfoList = uiState.pathInfoList,
                            selectedFolder = { dbPath ->
                                viewModel.getPathByOs(dbPath).let { osPath ->
                                    scope.launch {
                                        settingsViewModel.getLibraryNameIfExist(osPath)
                                            ?.let { libraryName ->
                                                val isSuccess = viewModel.setFolderAsSelected(
                                                    path = osPath,
                                                    libraryName = libraryName
                                                )
                                                if (isSuccess) {
                                                    navigator.navigate(
                                                        route = Routes.main_route,
                                                        options = NavOptions(launchSingleTop = false),
                                                    )
                                                }
                                            }
                                    }
                                }
                            },
                            createFolder = { path, name ->
                                viewModel.createFolderAndGetPath(path, name)?.let { resultPath ->
                                    settingsViewModel.createAppSettingsFile(
                                        path = resultPath,
                                        libraryName = name,
                                        themeName = "Dark" //todo
                                    )
                                    viewModel.createDbPath(
                                        dbPath = resultPath,
                                        libraryName = name
                                    )
                                    navigator.navigate(
                                        route = Routes.main_route,
                                        options = NavOptions(launchSingleTop = false),
                                    )
                                }
                            },
                            selectedPathInfo = { pathInfo ->
                                viewModel.selectPathInfo(pathInfo)
                                navigator.navigate(
                                    route = Routes.main_route,
                                    options = NavOptions(launchSingleTop = false),
                                )
                            },
                            renamePath = { pathInfo, newName ->
                                val newPath = viewModel.renamePath(
                                    pathInfo = pathInfo,
                                    newName = newName,
                                )
                                settingsViewModel.updateLibraryNameInFile(
                                    path = newPath,
                                    oldName = pathInfo.libraryName,
                                    newName = newName
                                )
                            },
                            restartApp = { restartWindow?.invoke() }
                        )
                    }
                }
                if (platform.isDesktop() && tooltip.value.showTooltip) {
                    ShowTooltip(tooltip.value)
                }
            }
        }
    }
}