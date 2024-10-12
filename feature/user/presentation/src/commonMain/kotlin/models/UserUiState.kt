package models

import AppConfig
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.books.UserBooksStatisticsData
import main_models.rating_review.ReviewAndRatingVo
import main_models.user.UserVo

data class UserUiState(
    private val appConfig: AppConfig,
    val isSignUnState: MutableState<Boolean> = mutableStateOf(false),
    val userInfo: MutableState<UserVo> = mutableStateOf(UserVo(0, "", "", false, false)),
    val userBooksStatistics: MutableState<UserBooksStatisticsData> = mutableStateOf(
        UserBooksStatisticsData()
    ),
    val showAdminPanel: MutableState<Boolean> = mutableStateOf(false),
    val userReviews: MutableState<List<ReviewAndRatingVo>> = mutableStateOf(emptyList())
) : BaseUIState