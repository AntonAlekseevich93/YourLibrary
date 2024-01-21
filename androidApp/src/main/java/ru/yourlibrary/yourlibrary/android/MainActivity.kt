package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import PlatformInfo
import PlatformSDK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
                Application(platform = Platform.MOBILE)
            }
        }
    }
}