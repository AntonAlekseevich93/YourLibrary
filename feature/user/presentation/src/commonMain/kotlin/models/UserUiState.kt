package models

import AppConfig
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.user.UserVo

data class UserUiState(
    private val appConfig: AppConfig,
    val isSignUnState: MutableState<Boolean> = mutableStateOf(false),
    val isAuthorized: MutableState<Boolean> = mutableStateOf(false),
    val userInfo: MutableState<UserVo> = mutableStateOf(UserVo(0L, "", "", false))
) : BaseUIState