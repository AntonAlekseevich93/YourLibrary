package ru.yourlibrary.yourlibrary.android

import AppTheme
import Application
import PlatformSDK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import application_platform.ApplicationPlatform
import di.PlatformConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlatformSDK.init(PlatformConfiguration(this))
        setContent {
            AppTheme {
                Application(platform = ApplicationPlatform.MOBILE)
            }
        }
    }
}