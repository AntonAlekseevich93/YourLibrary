package main_models.rest.rating_review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReviewsAndRatingsResponseContent(
    @SerialName("allReviewsAndRatings") val reviewsAndRatings: List<ReviewAndRatingRemoteDto>? = null,
    @SerialName("reviewAndRating") val reviewAndRating: ReviewAndRatingRemoteDto? = null,
    @SerialName("isSuccess") val isSuccess: Boolean
)