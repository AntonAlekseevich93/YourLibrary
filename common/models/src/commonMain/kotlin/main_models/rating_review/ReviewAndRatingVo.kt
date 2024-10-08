package main_models.rating_review

import kotlinx.serialization.Serializable
import main_models.books.LANG

@Serializable
class ReviewAndRatingVo(
    val localId: Int?,
    val id: Int?,
    val ratingScore: Int,
    val reviewText: String?,
    val bookId: String,
    val bookAuthorId: String,
    val userId: Int,
    val userName: String,
    val likesCount: Int?,
    val dislikesCount: Int?,
    val answersCount: Int?,
    val isApprovedReview: Boolean,
    val isDisapprovedReview: Boolean,
    val timestampOfCreatingScore: Long,
    val timestampOfUpdatingScore: Long,
    val timestampOfCreatingReview: Long,
    val timestampOfUpdatingReview: Long,
    val updatedByDeviceId: String?,
    val bookGenreId: Int,
    val isCreatedManuallyBook: Boolean,
    val bookForAllUsers: Boolean,
    val mainBookId: String,
    val fromParsing: Boolean,
    val lang: LANG,
    val parsingSourceId: Long,
) {

    companion object {
        fun createEmptyRating(
            rating: Int,
            bookId: String,
            bookAuthorId: String,
            bookGenreId: Int,
            isCreatedManuallyBook: Boolean,
            bookForAllUsers: Boolean,
            userId: Int,
            userName: String,
            deviceId: String,
            timestamp: Long,
            mainBookId: String,
        ): ReviewAndRatingVo = ReviewAndRatingVo(
            id = null,
            localId = null,
            ratingScore = rating,
            reviewText = null,
            bookId = bookId,
            bookAuthorId = bookAuthorId,
            userId = userId,
            userName = userName,
            likesCount = 0,
            dislikesCount = 0,
            answersCount = 0,
            isApprovedReview = false,
            isDisapprovedReview = false,
            timestampOfCreatingScore = timestamp,
            timestampOfUpdatingScore = timestamp,
            timestampOfCreatingReview = 0,
            timestampOfUpdatingReview = 0,
            updatedByDeviceId = deviceId,
            bookGenreId = bookGenreId,
            isCreatedManuallyBook = isCreatedManuallyBook,
            bookForAllUsers = bookForAllUsers,
            mainBookId = mainBookId,
            lang = LANG.RUSSIAN,
            fromParsing = false,
            parsingSourceId = -1,
        )
    }
}