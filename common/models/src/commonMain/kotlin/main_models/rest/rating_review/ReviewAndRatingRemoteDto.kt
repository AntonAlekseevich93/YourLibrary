package main_models.rest.rating_review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.books.toLang
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
    @SerialName("mainBookId") val mainBookId: String,
    @SerialName("fromParsing") val fromParsing: Boolean,
    @SerialName("lang") val lang: String,
    @SerialName("parsingSourceId") val parsingSourceId: Long?,
)

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
        mainBookId = mainBookId,
        lang = lang.toLang(),
        fromParsing = fromParsing,
        parsingSourceId = parsingSourceId ?: -1
    )
}

fun ReviewAndRatingVo.toRemoteDto(): ReviewAndRatingRemoteDto {
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
        mainBookId = mainBookId,
        fromParsing = fromParsing,
        lang = lang.value,
        parsingSourceId = parsingSourceId
    )
}