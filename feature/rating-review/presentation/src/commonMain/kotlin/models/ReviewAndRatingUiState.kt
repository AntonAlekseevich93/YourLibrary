package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.rating_review.ReviewAndRatingVo

data class ReviewAndRatingUiState(
    val reviews: MutableState<List<ReviewAndRatingVo>> = mutableStateOf(emptyList())
) : BaseUIState