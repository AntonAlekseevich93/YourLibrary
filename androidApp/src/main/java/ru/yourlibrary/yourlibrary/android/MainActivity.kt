package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import NavigationHandler
import PlatformSDK
import Routes
import TooltipHandler
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import di.PlatformConfiguration
import kotlinx.coroutines.launch
import main_models.TooltipItem
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator
import platform.Platform
import platform.PlatformInfoData


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigator: MutableState<Navigator?> = mutableStateOf(null)
        val desktopTooltip = mutableStateOf(TooltipItem())
        val currentRoute = mutableStateOf("")

        window.navigationBarColor = ContextCompat.getColor(this, R.color.your_navigation_bar_color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }

        PlatformSDK.init(
            configuration = PlatformConfiguration(applicationContext),
            platformInfo = PlatformInfoData(
                canUseModifierBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                hazeBlurEnabled = Build.VERSION.SDK_INT >= MIN_SDK_HAZE_BLUR_VERSION,
            ),
            platform = Platform.MOBILE(),
            navigationHandler = createNavigationHandler(
                navigator = navigator,
                desktopTooltip = desktopTooltip,
                currentRoute = currentRoute,
            ),
            tooltipHandler = createTooltipHandler(desktopTooltip)
        )

        setContent {
            val platformDisplayHeight = LocalConfiguration.current.screenHeightDp.dp
            val scope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit) {
                scope.launch {
                    navigator.value?.currentEntry?.collect {
                        currentRoute.value = it?.route?.route ?: ""
                    }
                }
            }
            AppTheme {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    PreComposeApp {
                        navigator.value = rememberNavigator()
                        Application(
                            platform = Platform.MOBILE(),
                            isKeyboardShown = keyboardAsState(),
                            navigator = navigator.value ?: rememberNavigator(),
                            platformDisplayHeight = platformDisplayHeight
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val MIN_SDK_HAZE_BLUR_VERSION = 32
    }
}

fun createNavigationHandler(
    navigator: MutableState<Navigator?>,
    desktopTooltip: MutableState<TooltipItem>,
    currentRoute: State<String>,
): NavigationHandler {
    var lastScreenRouteBeforeBookInfo: String = Routes.main_route

    val handler = object : NavigationHandler {
        override fun navigateToSearch() {

        }

        override fun navigateToSelectorVault(needPopBackStack: Boolean) {
            if (currentRoute.value != Routes.vault_route) {
                desktopTooltip.value.showTooltip = false
                //todo здесь происходит мигание анимации, но без этого баг. Подумать
                if (needPopBackStack) navigator.value?.popBackStack()
                navigator.value?.navigate(route = Routes.vault_route)
            }
        }

        override fun navigateToBookCreator(popUpToMain: Boolean) {
            if (currentRoute.value != Routes.book_creator_route) {
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
        }

        override fun goBack() {
            desktopTooltip.value.showTooltip = false
            navigator.value?.goBack()
        }

        override fun navigateToMain() {
            if (currentRoute.value != Routes.main_route) {
                navigator.value?.navigate(
                    route = Routes.main_route,
                    options = NavOptions(launchSingleTop = false),
                )
            }
        }

        override fun restartWindow() {
            //nop
        }

        override fun navigateToBookInfo() {
            if (currentRoute.value != Routes.book_info_route) {
                lastScreenRouteBeforeBookInfo = currentRoute.value
            }
            navigator.value?.navigate(
                route = Routes.book_info_route,
            )
        }

        override fun closeBookInfoScreen() {
            navigator.value?.goBack(PopUpTo(route = lastScreenRouteBeforeBookInfo))
        }

        override fun navigateToAuthorsScreen() {
            if (currentRoute.value != Routes.authors_screen_route) {
                navigator.value?.navigate(
                    route = Routes.authors_screen_route,
                )
            }
        }

        override fun navigateToJoinAuthorsScreen() {
            if (currentRoute.value != Routes.join_authors_screen_route) {
                navigator.value?.navigate(
                    route = Routes.join_authors_screen_route
                )
            }
        }

        override fun navigateToSettingsScreen() {
            if (currentRoute.value != Routes.settings_screen_route) {
                navigator.value?.navigate(
                    route = Routes.settings_screen_route
                )
            }
        }

        override fun navigateToProfile() {
            if (currentRoute.value != Routes.profile_screen_route) {
                navigator.value?.navigate(
                    route = Routes.profile_screen_route
                )
            }
        }

        override fun navigateToAdminPanel() {
            if (currentRoute.value != Routes.admin_screen_route) {
                navigator.value?.navigate(
                    route = Routes.admin_screen_route
                )
            }
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