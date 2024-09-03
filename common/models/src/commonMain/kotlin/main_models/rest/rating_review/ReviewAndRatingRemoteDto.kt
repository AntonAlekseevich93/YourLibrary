package main_models.rest.rating_review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rating_review.ReviewAndRatingVo

@Serializable
data class ReviewAndRatingRemoteDto(
    @SerialName("id") val id: Int?,
    @SerialName("ratingScore") val ratingScore: Int,
    @SerialName("reviewText") val reviewText: String?,
    @SerialName("bookId") val bookId: String,
    @SerialName("bookAuthorId") val bookAuthorId: String,
    @SerialName("userId") val userId: Int?,
    @SerialName("userName") val userName: String,
    @SerialName("likesCount") val likesCount: Int?,
    @SerialName("dislikesCount") val dislikesCount: Int?,
    @SerialName("answersCount") val answersCount: Int?,
    @SerialName("isApprovedReview") val isApprovedReview: Boolean,
    @SerialName("isDisapprovedReview") val isDisapprovedReview: Boolean,
    @SerialName("timestampOfCreatingScore") val timestampOfCreatingScore: Long,
    @SerialName("timestampOfUpdatingScore") val timestampOfUpdatingScore: Long,
    @SerialName("timestampOfCreatingReview") val timestampOfCreatingReview: Long,
    @SerialName("timestampOfUpdatingReview") val timestampOfUpdatingReview: Long,
    @SerialName("updatedByDeviceId") val updatedByDeviceId: String?,
    @SerialName("bookGenreId") val bookGenreId: Int,
    @SerialName("isCreatedManuallyBook") val isCreatedManuallyBook: Boolean,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean,
    @SerialName("mainBookId") val mainBookId: String,
)

fun ReviewAndRatingVo.toDto(): ReviewAndRatingRemoteDto {
    return ReviewAndRatingRemoteDto(
        id = id,
        ratingScore = ratingScore,
        reviewText = reviewText,
        bookId = bookId,
        bookAuthorId = bookAuthorId,
        userId = userId,
        userName = userName,
        likesCount = likesCount,
        dislikesCount = dislikesCount,
        answersCount = answersCount,
        isApprovedReview = isApprovedReview,
        isDisapprovedReview = isDisapprovedReview,
        timestampOfCreatingScore = timestampOfCreatingScore,
        timestampOfUpdatingScore = timestampOfUpdatingScore,
        timestampOfCreatingReview = timestampOfCreatingReview,
        timestampOfUpdatingReview = timestampOfUpdatingReview,
        updatedByDeviceId = updatedByDeviceId,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
        mainBookId = mainBookId
    )
}

fun ReviewAndRatingRemoteDto.toVo(): ReviewAndRatingVo? {
    return ReviewAndRatingVo(
        id = id ?: return null,
        localId = null,
        ratingScore = ratingScore ?: return null,
        reviewText = reviewText,
        bookId = bookId,
        bookAuthorId = bookAuthorId,
        userId = userId ?: return null,
        userName = userName,
        likesCount = likesCount,
        dislikesCount = dislikesCount,
        answersCount = answersCount,
        isApprovedReview = isApprovedReview,
        isDisapprovedReview = isDisapprovedReview,
        timestampOfCreatingScore = timestampOfCreatingScore,
        timestampOfUpdatingScore = timestampOfUpdatingScore,
        timestampOfCreatingReview = timestampOfCreatingReview,
        timestampOfUpdatingReview = timestampOfUpdatingReview,
        updatedByDeviceId = updatedByDeviceId,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
        mainBookId = mainBookId
    )
}

fun ReviewAndRatingVo.toRemoteDto(): ReviewAndRatingRemoteDto? {
    return ReviewAndRatingRemoteDto(
        id = id,
        ratingScore = ratingScore,
        reviewText = reviewText,
        bookId = bookId,
        bookAuthorId = bookAuthorId,
        userId = userId,
        userName = userName,
        likesCount = likesCount,
        dislikesCount = dislikesCount,
        answersCount = answersCount,
        isApprovedReview = isApprovedReview,
        isDisapprovedReview = isDisapprovedReview,
        timestampOfCreatingScore = timestampOfCreatingScore,
        timestampOfUpdatingScore = timestampOfUpdatingScore,
        timestampOfCreatingReview = timestampOfCreatingReview,
        timestampOfUpdatingReview = timestampOfUpdatingReview,
        updatedByDeviceId = updatedByDeviceId,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
        mainBookId = mainBookId
    )
}