package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.books.UserBooksStatisticsData
import main_models.rating_review.ReviewAndRatingVo
import main_models.user.UserVo

data class UserUiState(
    val isSignUnState: MutableState<Boolean> = mutableStateOf(false),
    val userInfo: MutableState<UserVo> = mutableStateOf(
        UserVo(
            0,
            "",
            "",
            false,
            false,
            true,
            timestampOfUpdating = 0L,
            null,
        )
    ),
    val userBooksStatistics: MutableState<UserBooksStatisticsData> = mutableStateOf(
        UserBooksStatisticsData()
    ),
    val showAdminPanel: MutableState<Boolean> = mutableStateOf(false),
    val userReviews: MutableState<List<ReviewAndRatingVo>> = mutableStateOf(emptyList()),
    val finishedThisYearBooksCount: MutableState<Int> = mutableStateOf(0),
) : BaseUIState

//todo move to mock file
fun getUserUiStateMock(): UserUiState {
    return UserUiState(
        userInfo = mutableStateOf(
            UserVo(
                0,
                "Антон Алексеевич",
                email = "anton.alex@gmail.com",
                isVerified = false,
                isAuth = true,
                true,
                timestampOfUpdating = 0L,
                null
            )
        ),
        userBooksStatistics = mutableStateOf(
            UserBooksStatisticsData(
                24, 10, 1, 12, 20, emptyList(), 2014,
            )
        ),
        showAdminPanel = mutableStateOf(true)
    )
}