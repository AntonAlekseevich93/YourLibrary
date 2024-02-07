package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import NavigationHandler
import PlatformInfo
import PlatformSDK
import TooltipHandler
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import di.PlatformConfiguration
import main_models.TooltipItem
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import platform.Platform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigator: MutableState<Navigator?> = mutableStateOf(null)
        val desktopTooltip = mutableStateOf(TooltipItem())

        PlatformSDK.init(
            configuration = PlatformConfiguration(this),
            platformInfo = PlatformInfo(),
            platform = Platform.MOBILE,
            navigationHandler = createNavigationHandler(
                navigator = navigator,
                desktopTooltip = desktopTooltip
            ),
            tooltipHandler = createTooltipHandler(desktopTooltip)
        )

        setContent {
            AppTheme {
                PreComposeApp {
                    navigator.value = rememberNavigator()
                    Application(
                        platform = Platform.MOBILE,
                        isKeyboardShown = keyboardAsState(),
                        navigator = navigator.value ?: rememberNavigator()
                    )
                }
            }
        }
    }
}

fun createNavigationHandler(
    navigator: MutableState<Navigator?>,
    desktopTooltip: MutableState<TooltipItem>
): NavigationHandler {
    val handler = object : NavigationHandler {
        override fun navigateToSearch() {

        }

        override fun navigateToSelectorVault(needPopBackStack: Boolean) {
            desktopTooltip.value.showTooltip = false
            //todo здесь происходит мигание анимации, но без этого баг. Подумать
            if (needPopBackStack) navigator.value?.popBackStack()
            navigator.value?.navigate(route = Routes.vault_route)
        }

        override fun navigateToBookCreator(popUpToMain: Boolean) {
            val options = if (popUpToMain)
                NavOptions(popUpTo = PopUpTo(Routes.main_route))
            else {
                NavOptions(launchSingleTop = false)
            }

            navigator.value?.navigate(
                route = Routes.book_creator_route,
                options = options,
            )
        }

        override fun goBack() {
            desktopTooltip.value.showTooltip = false
            navigator.value?.goBack()
        }

        override fun navigateToMain() {
            navigator.value?.navigate(
                route = Routes.main_route,
                options = NavOptions(launchSingleTop = false),
            )
        }

        override fun restartWindow() {
            //nop
        }

        override fun navigateToBookInfo() {
            navigator.value?.navigate(
                route = Routes.book_info_route,
                options = NavOptions(popUpTo = PopUpTo.Prev),
            )
        }

        override fun navigateToAuthorsScreen() {
            navigator.value?.navigate(
                route = Routes.authors_screen_route,
            )
        }

    }
    return handler
}

fun createTooltipHandler(
    desktopTooltip: MutableState<TooltipItem>,
): TooltipHandler {
    val handler = object : TooltipHandler {
        override fun setTooltip(tooltip: TooltipItem) {
            desktopTooltip.value = tooltip
        }

    }
    return handler
}


@Composable
fun keyboardAsState(): State<Boolean> {
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    DisposableEffect(LocalWindowInfo.current) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            isImeVisible = (ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true)
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    return rememberUpdatedState(isImeVisible)
}