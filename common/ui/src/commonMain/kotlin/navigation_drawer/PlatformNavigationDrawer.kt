package navigation_drawer

import ApplicationTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import platform.Platform

@Composable
fun PlatformNavigationDrawer(
    platform: Platform,
    leftDrawerContent: @Composable () -> Unit = {},
    rightDrawerContent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    leftDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    showLeftDrawer: MutableState<Boolean> = mutableStateOf(false),
    showRightDrawer: MutableState<Boolean> = mutableStateOf(false),
    background: Color = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
    content: @Composable () -> Unit,
) {
    when (platform) {
        Platform.DESKTOP -> {
            CustomDrawer(
                leftDrawerContent = leftDrawerContent,
                rightDrawerContent = rightDrawerContent,
                modifier = modifier,
                showLeftDrawer = showLeftDrawer.value,
                showRightDrawer = showRightDrawer.value,
                background = background,
                content = content
            )
        }

        else -> {
            CustomDrawer(
                leftDrawerContent = leftDrawerContent,
                rightDrawerContent = rightDrawerContent,
                modifier = modifier,
                showLeftDrawer = showLeftDrawer.value,
                showRightDrawer = showRightDrawer.value,
                background = background,
                content = content
            )
        }
    }
}

/**
 * We use this solution because DismissibleNavigationDrawer has a bug with offset
 * in compose (multiplatoform 1.5.3. version)
 */
@Composable
fun CustomDrawer(
    leftDrawerContent: @Composable () -> Unit,
    rightDrawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    background: Color,
    showLeftDrawer: Boolean,
    showRightDrawer: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxHeight()
            .background(background)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = showLeftDrawer) {
                Box(Modifier.weight(1f)) {
                    leftDrawerContent()
                }
            }
            Box(Modifier.weight(1f)) {
                content()
            }
            AnimatedVisibility(visible = showRightDrawer) {
                Box(Modifier.weight(1f)) {
                    rightDrawerContent()
                }
            }
        }
    }
}