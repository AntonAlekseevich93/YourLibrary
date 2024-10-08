package main_models.rest.rating_review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rating_review.ReviewAndRatingVo

@Serializable
class ReviewRemoteDto(
    @SerialName("reviewText") val reviewText: String,
    @SerialName("bookId") val bookId: String,
    @SerialName("mainBookId") val mainBookId: String,
    @SerialName("lang") val lang: String,
)

fun ReviewAndRatingVo.toRemoteReview(): ReviewRemoteDto? {
    return ReviewRemoteDto(
        reviewText = reviewText ?: return null,
        bookId = bookId,
        mainBookId = mainBookId,
        lang = lang.value
    )
}