package main_models.rest.rating_review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rating_review.ReviewAndRatingVo

@Serializable
class RatingRemoteDto(
    @SerialName("rating") val rating: Int,
    @SerialName("bookId") val bookId: String,
    @SerialName("bookAuthorId") val bookAuthorId: String,
    @SerialName("bookGenreId") val bookGenreId: Int,
    @SerialName("isCreatedManuallyBook") val isCreatedManuallyBook: Boolean,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean,
    @SerialName("mainBookId") val mainBookId: String,
)

fun ReviewAndRatingVo.toRemoteRating(): RatingRemoteDto =
    RatingRemoteDto(
        rating = ratingScore,
        bookId = bookId,
        bookAuthorId = bookAuthorId,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
        mainBookId = mainBookId
    )