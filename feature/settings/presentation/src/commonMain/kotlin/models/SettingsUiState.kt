package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import platform.Platform

class SettingsUiState(
    val platform: Platform,
    val isHazeBlurEnabled: MutableState<Boolean> = mutableStateOf(true),
) : BaseUIState