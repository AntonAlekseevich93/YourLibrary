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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.Inject
import platform.Platform
import platform.isDesktop
import platform.isMobile
import screens.BookScreen
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
    val showNote = remember { mutableStateOf(false) }
    val fullScreenNote = remember { mutableStateOf(false) }
    if (showNote.value && platform.isMobile()) {
        fullScreenNote.value = true
    }
    val tooltip = remember { mutableStateOf(TooltipItem()) }


    AppTheme {
        Box(modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)) {
            AnimatedVisibility(
                visible = !fullScreenNote.value,
                enter = fadeIn(),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
            ) {
                MainScreen(
                    uiState = uiState,
                    platform = platform,
                    showLeftDrawer = showLeftDrawer,
                    showNote = showNote,
                    showSearch = showSearch,
                    leftDrawerState = leftDrawerState,
                    viewModel = viewModel,
                    tooltipCallback = {
                        tooltip.value = it
                    }
                )
            }

            AnimatedVisibility(
                visible = showNote.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BookScreen(
                    uiState = uiState,
                    platform = platform,
                    showLeftDrawer = showLeftDrawer,
                    showRightDrawer = showRightDrawer,
                    showNote = showNote,
                    showSearch = showSearch,
                    leftDrawerState = leftDrawerState,
                    rightDrawerState = rightDrawerState,
                    viewModel = viewModel,
                    fullScreenNote = fullScreenNote,
                    tooltipCallback = {
                        tooltip.value = it
                    },
                    onClose = {
                        showNote.value = false
                        fullScreenNote.value = false
                    }
                )
            }

            if (platform.isDesktop() && tooltip.value.showTooltip) {
                ShowTooltip(tooltip.value)
            }
        }
    }
}
