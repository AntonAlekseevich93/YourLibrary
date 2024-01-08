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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import di.Inject
import io.kamel.core.Resource
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import platform.Platform
import platform.isDesktop
import platform.isMobile
import screens.MainScreen
import tooltip_area.ShowTooltip
import tooltip_area.TooltipItem

@Composable
fun Application(platform: Platform) {
    val viewModel = remember { Inject.instance<ApplicationViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val leftDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val rightDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showLeftDrawer = remember { mutableStateOf(false) }
    val showRightDrawer = remember { mutableStateOf(false) }
    val showSearch = remember { mutableStateOf(false) }
    val fullScreenBookInfo = remember { mutableStateOf(false) }
    val tooltip = remember { mutableStateOf(TooltipItem()) }
    val painterSelectedBookInCache: MutableState<Resource<Painter>?> = mutableStateOf(null)
    val selectedBookId: MutableState<Int> = mutableStateOf(-1)

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
                    initialRoute = Routes.main_route,
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
                            uiState = uiState,
                            platform = platform,
                            showLeftDrawer = showLeftDrawer,
                            showSearch = showSearch,
                            leftDrawerState = leftDrawerState,
                            viewModel = viewModel,
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
                                navigator.goBack()
                            },
                            createBookListener = {
                                navigator.navigate(
                                    route = Routes.book_creator_route,
                                    options = NavOptions(popUpTo = PopUpTo(Routes.main_route)),
                                )
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
                }
                if (platform.isDesktop() && tooltip.value.showTooltip) {
                    ShowTooltip(tooltip.value)
                }
            }
        }
    }
}