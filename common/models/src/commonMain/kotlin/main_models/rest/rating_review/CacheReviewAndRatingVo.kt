package main_models.rest.rating_review

import kotlinx.serialization.SerialName

data class CacheReviewAndRatingVo(
    @SerialName("cacheLocalId") val localId: Long? = null,
    @SerialName("cacheReviewAndRating")
    val cacheReviewAndRating: ReviewAndRatingRemoteDto?,
    @SerialName("cacheMainBookId") val cacheMainBookId: String,
    @SerialName("cacheTimestamp") val cacheTimestamp: Long,
    @SerialName("cacheUserId") val cacheUserId: Int,
)

fun CacheReviewAndRatingVo.toRemoteDto(): ReviewAndRatingRemoteDto? {
    return if (cacheReviewAndRating == null) null
    else ReviewAndRatingRemoteDto(
        id = cacheReviewAndRating.id,
        ratingScore = cacheReviewAndRating.ratingScore,
        reviewText = cacheReviewAndRating.reviewText,
        bookId = cacheReviewAndRating.bookId,
        bookAuthorId = cacheReviewAndRating.bookAuthorId,
        userId = cacheReviewAndRating.userId,
        userName = cacheReviewAndRating.userName,
        likesCount = cacheReviewAndRating.likesCount,
        dislikesCount = cacheReviewAndRating.dislikesCount,
        answersCount = cacheReviewAndRating.answersCount,
        isApprovedReview = cacheReviewAndRating.isApprovedReview,
        isDisapprovedReview = cacheReviewAndRating.isDisapprovedReview,
        timestampOfCreatingScore = cacheReviewAndRating.timestampOfCreatingScore,
        timestampOfUpdatingScore = cacheReviewAndRating.timestampOfUpdatingScore,
        timestampOfCreatingReview = cacheReviewAndRating.timestampOfCreatingReview,
        timestampOfUpdatingReview = cacheReviewAndRating.timestampOfUpdatingReview,
        updatedByDeviceId = cacheReviewAndRating.updatedByDeviceId,
        bookGenreId = cacheReviewAndRating.bookGenreId,
        isCreatedManuallyBook = cacheReviewAndRating.isCreatedManuallyBook,
        isServiceDevelopmentBook = cacheReviewAndRating.isServiceDevelopmentBook,
        mainBookId = cacheReviewAndRating.mainBookId,
        lang = cacheReviewAndRating.lang,
        fromParsing = cacheReviewAndRating.fromParsing,
        parsingSourceId = cacheReviewAndRating.parsingSourceId,
    )
}