package screens

import ApplicationTheme
import ApplicationUiState
import ApplicationViewModel
import BookInfoScreen
import CustomDockedSearchBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import menu_bar.LeftMenuBar
import navigation_drawer.PlatformLeftDrawerContent
import navigation_drawer.PlatformNavigationDrawer
import platform.Platform
import tooltip_area.TooltipItem

@Composable
fun BookScreen(
    uiState: ApplicationUiState,
    platform: Platform,
    showNote: MutableState<Boolean>,
    fullScreenNote: MutableState<Boolean>,
    showSearch: MutableState<Boolean>,
    showLeftDrawer: MutableState<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    leftDrawerState: DrawerState,
    rightDrawerState: DrawerState,
    viewModel: ApplicationViewModel,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    onClose: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val leftMenuVisible = remember { mutableStateOf(false) }
    val background = if (fullScreenNote.value || leftMenuVisible.value)
        ApplicationTheme.colors.mainBackgroundWindowDarkColor
    else {
        Color.Transparent
    }

    /** this is necessary to get rid of the white blinking effect due
     * to the background transparent when collapsing and expanding */
    LaunchedEffect(fullScreenNote.value) {
        if (!fullScreenNote.value && leftMenuVisible.value) {
            scope.launch {
                delay(300)
                leftMenuVisible.value = false
            }
        }
    }

    Row(
        modifier = Modifier.background(background)
    ) {
        AnimatedVisibility(
            visible = fullScreenNote.value,
        ) {
            if (fullScreenNote.value) {
                /** this is necessary to get rid of the white blinking effect due
                 * to the background transparent when collapsing and expanding */
                scope.launch {
                    delay(200)
                    leftMenuVisible.value = true
                }
            }
            LeftMenuBar(
                searchListener = {
                    showSearch.value = true
                },
                tooltipCallback = tooltipCallback,
                open = {
                    showNote.value = true
                },
            )
        }

        PlatformNavigationDrawer(
            platform = platform,
            leftDrawerContent = {
                AnimatedVisibility(visible = fullScreenNote.value) {
                    Row {
                        PlatformLeftDrawerContent(
                            platform = platform,
                            tooltipCallback = tooltipCallback,
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
                            }
                        )
                        Divider(
                            modifier = Modifier.fillMaxHeight().width(1.dp),
                            thickness = 1.dp,
                            color = ApplicationTheme.colors.divider
                        )
                    }
                }
            },
            background = background,
            showLeftDrawer = showLeftDrawer,
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
            ) {
                BookInfoScreen(
                    platform = platform,
                    onClose = onClose,
                    fullScreenNote = fullScreenNote,
                    showLeftDrawer = showLeftDrawer,
                    showRightDrawer = showRightDrawer,
                    openLeftDrawerListener = {
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
                    openRightDrawerListener = {
                        scope.launch {
                            if (!showRightDrawer.value) {
                                showRightDrawer.value = true
                                rightDrawerState.open()
                            } else {
                                showRightDrawer.value = false
                                rightDrawerState.close()
                            }
                        }
                    },
                    closeRightDrawerListener = {
                        scope.launch {
                            if (!showRightDrawer.value) {
                                showRightDrawer.value = true
                                rightDrawerState.open()
                            } else {
                                showRightDrawer.value = false
                                rightDrawerState.close()
                            }
                        }
                    },
                    tooltipCallback = tooltipCallback
                )

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