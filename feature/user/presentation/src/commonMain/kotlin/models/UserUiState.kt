package models

import AppConfig
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.books.UserBooksStatistics
import main_models.user.UserVo

data class UserUiState(
    private val appConfig: AppConfig,
    val isSignUnState: MutableState<Boolean> = mutableStateOf(false),
    val userInfo: MutableState<UserVo> = mutableStateOf(UserVo(0, "", "", false, false)),
    val userBooksStatistics: MutableState<UserBooksStatistics> = mutableStateOf(UserBooksStatistics()),
    val showAdminPanel: MutableState<Boolean> = mutableStateOf(false)
) : BaseUIState