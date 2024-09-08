package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import PlatformSDK
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arkivanov.decompose.defaultComponentContext
import di.PlatformConfiguration
import main_models.TooltipItem
import navigation.DefaultRootComponent
import platform.Platform
import platform.PlatformInfoData


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val desktopTooltip = mutableStateOf(TooltipItem())
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
        val root = DefaultRootComponent(
            componentContext = defaultComponentContext(),
        )
        PlatformSDK.init(
            configuration = PlatformConfiguration(applicationContext),
            platformInfo = PlatformInfoData(
                canUseModifierBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                hazeBlurEnabled = Build.VERSION.SDK_INT >= MIN_SDK_HAZE_BLUR_VERSION,
            ),
            platform = Platform.MOBILE(),
            tooltipHandler = createTooltipHandler(desktopTooltip)
        )

        setContent {
            val platformDisplayHeight = LocalConfiguration.current.screenHeightDp.dp
            AppTheme {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    Application(
                        platform = Platform.MOBILE(),
                        platformDisplayHeight = platformDisplayHeight,
                        component = root
                    )
                }
            }
        }
    }

    companion object {
        private const val MIN_SDK_HAZE_BLUR_VERSION = 32
    }
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