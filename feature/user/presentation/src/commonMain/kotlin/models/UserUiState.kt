package models

import AppConfig
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.user.UserVo

data class UserUiState(
    private val appConfig: AppConfig,
    val isSignUnState: MutableState<Boolean> = mutableStateOf(false),
    val userInfo: MutableState<UserVo> = mutableStateOf(UserVo(0, "", "", false, false)),
    val userBooksStatistics: MutableState<UserBooksStatistics> = mutableStateOf(
        UserBooksStatistics(
            30, 4, 6, 19, 1, 8, 5
        )
    ),
    val showAdminPanel: MutableState<Boolean> = mutableStateOf(false)
) : BaseUIState

data class UserBooksStatistics(
    val allBooksCount: Int = 0,
    val plannedBooksCount: Int = 0,
    val readingBooksCount: Int = 0,
    val doneBooksCount: Int = 0,
    val deferredBooksCount: Int = 0,
    val plannedThisYearBooks: Int = 0,
    val finishedThisYearBooks: Int = 0,
)