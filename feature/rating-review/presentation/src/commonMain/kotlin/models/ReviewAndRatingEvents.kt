package models

import BaseEvent
import main_models.rating_review.ReviewAndRatingVo

sealed class ReviewAndRatingEvents : BaseEvent {
    data class SetReviews(val reviews: List<ReviewAndRatingVo>) : ReviewAndRatingEvents()
}