import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
    val selectedScreen = remember { mutableStateOf(ScreenType.MAIN_SCREEN) }
    val fullScreenBookInfo = remember { mutableStateOf(false) }
    if (selectedScreen.value == ScreenType.BOOK_INFO && platform.isMobile()) {
        fullScreenBookInfo.value = true
    }
    val tooltip = remember { mutableStateOf(TooltipItem()) }
    val painterSelectedBookInCache: MutableState<Resource<Painter>?> = mutableStateOf(null)
    val selectedBookId: MutableState<Int> = mutableStateOf(-1)

    AppTheme {
        Box(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)) {
            AnimatedVisibility(
                visible = !fullScreenBookInfo.value,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
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
                        selectedScreen.value = ScreenType.BOOK_INFO
                    },
                    tooltipCallback = {
                        tooltip.value = it
                    },
                    createBookListener = {
                        selectedScreen.value = ScreenType.BOOK_CREATOR
                    }
                )
            }

            AnimatedVisibility(
                visible = selectedScreen.value == ScreenType.BOOK_INFO,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
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
                        selectedScreen.value = ScreenType.MAIN_SCREEN
                        fullScreenBookInfo.value = false
                    },
                    createBookListener = {
                        selectedScreen.value = ScreenType.BOOK_CREATOR
                    }
                )
            }

            AnimatedVisibility(
                visible = selectedScreen.value == ScreenType.BOOK_CREATOR,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BookCreatorScreen(
                    platform = platform,
                    fullScreenBookCreator = mutableStateOf(false),
                    showRightDrawer = showRightDrawer,
                    tooltipCallback = { tooltip.value = it },
                    closeBookCreatorListener = {
                        selectedScreen.value = ScreenType.MAIN_SCREEN
                    }
                )
            }

            if (platform.isDesktop() && tooltip.value.showTooltip) {
                ShowTooltip(tooltip.value)
            }
        }
    }
}

private enum class ScreenType {
    MAIN_SCREEN,
    BOOK_INFO,
    BOOK_CREATOR
}