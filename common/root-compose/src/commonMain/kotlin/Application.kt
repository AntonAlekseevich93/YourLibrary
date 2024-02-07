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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import di.Inject
import kotlinx.coroutines.launch
import main_models.TooltipItem
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import platform.Platform
import platform.isDesktop
import platform.isMobile
import screens.selecting_project.CreationAndSelectionProjectFolderScreen
import tooltip_area.ShowTooltip

@Composable
fun Application(
    platform: Platform,
    isKeyboardShown: State<Boolean> = mutableStateOf(false),
    navigator: Navigator,
    desktopTooltip: MutableState<TooltipItem>? = null,
) {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showSearch = remember { mutableStateOf(false) }
    val dbPathExist = remember { mutableStateOf(viewModel.isDbPathIsExist(platform)) }
    val scope = rememberCoroutineScope()

    AppTheme {
        navigator.currentEntry.collectAsState(null).value?.route?.route?.let { currentRoute ->
            if (!uiState.fullScreenBookInfo.value && platform.isMobile() && currentRoute == Routes.main_route) {
                uiState.fullScreenBookInfo.value = true
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
                        platform = platform,
                        showLeftDrawer = uiState.showLeftDrawerState,
                        showSearch = showSearch,
                        leftDrawerState = leftDrawerState,
                    )
                }

                dialog(route = Routes.book_info_route) {
                    BookScreen(
                        platform = platform,
                        bookItemId = uiState.selectedBookId.value,
                        showLeftDrawer = uiState.showLeftDrawerState,
                        showRightDrawer = uiState.showRightDrawerState,
                        showSearch = showSearch,
                        fullScreenBookInfo = uiState.fullScreenBookInfo,
                        painterInCache = uiState.painterSelectedBookInCache.value,
                        isKeyboardShown = isKeyboardShown,
                    )
                }

                dialog(route = Routes.book_creator_route) {
                    BookCreatorScreen(
                        platform = platform,
                        fullScreenBookCreator = mutableStateOf(false),
                        isKeyboardShown = isKeyboardShown,
                        showRightDrawer = uiState.showRightDrawerState,
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
                        createTransition = fadeIn(tween(1)),
                        destroyTransition = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    )
                ) {
                    AuthorsScreen()
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