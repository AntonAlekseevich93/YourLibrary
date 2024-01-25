package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import PlatformInfo
import PlatformSDK
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import platform.Platform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlatformSDK.init(
            configuration = PlatformConfiguration(this),
            platformInfo = PlatformInfo(),
            platform = Platform.MOBILE,
        )
        setContent {
            AppTheme {
                Application(platform = Platform.MOBILE, isKeyboardShown = keyboardAsState())
            }
        }
    }
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