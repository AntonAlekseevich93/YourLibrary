import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import animations.SuccessAnimation
import bottom_sheets.ActionBottomSheet
import buttons.MenuButton
import dev.chrisbanes.haze.HazeState
import di.Inject
import elements.SettingsAppBar
import models.SettingsEvents
import navigation.screen_components.SettingsScreenComponent
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.clear_all_cache
import yourlibrary.common.resources.generated.resources.clear_cache_bottom_sheet_info
import yourlibrary.common.resources.generated.resources.dont_clear
import yourlibrary.common.resources.generated.resources.ic_storage_menu
import yourlibrary.common.resources.generated.resources.settings

@Composable
fun SettingsScreen(
    hazeState: HazeState,
    navigationComponent: SettingsScreenComponent,
) {
    val viewModel = remember { Inject.instance<SettingsViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    var showClearCacheBottomSheet by remember { mutableStateOf(false) }
    val showSuccessAnimation = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SettingsAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = uiState.isHazeBlurEnabled.value,
                title = stringResource(Res.string.settings),
                onClose = {},
                onBack = {
                    navigationComponent.onBack()
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ApplicationTheme.colors.cardBackgroundDark)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .background(ApplicationTheme.colors.cardBackgroundDark)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(
                            top = it.calculateTopPadding().plus(24.dp), bottom = 24.dp
                        )
                    ) {
                        MenuButton(
                            icon = Res.drawable.ic_storage_menu,
                            iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                            invoke = {
                                Text(
                                    text = stringResource(Res.string.clear_all_cache),
                                    style = ApplicationTheme.typography.headlineRegular,
                                    color = ApplicationTheme.colors.mainTextColor,

                                    )
                            },
                            onClick = {
                                showClearCacheBottomSheet = !showClearCacheBottomSheet
                            }
                        )
                    }
                }
            }
        }

        if (showClearCacheBottomSheet) {
            ActionBottomSheet(
                infoText = Res.string.clear_cache_bottom_sheet_info,
                successTitle = Res.string.clear_all_cache,
                dismissTitle = Res.string.dont_clear,
                onAccept = {
                    showSuccessAnimation.value = true
                    viewModel.sendEvent(SettingsEvents.ClearAllCache)
                },
                onDismiss = {
                    showClearCacheBottomSheet = !showClearCacheBottomSheet
                }
            )
        }

        SuccessAnimation(
            show = showSuccessAnimation,
            finishAnimation = {
                showSuccessAnimation.value = false
            }
        )
    }
}