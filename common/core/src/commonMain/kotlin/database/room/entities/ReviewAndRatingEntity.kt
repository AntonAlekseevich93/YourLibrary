package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.rating_review.ReviewAndRatingVo

@Entity
data class ReviewAndRatingEntity(
    @PrimaryKey(autoGenerate = true) @SerialName("localId") val localId: Long? = null,
    @SerialName("remoteId") val id: Int?,
    @SerialName("ratingScore") val ratingScore: Int,
    @SerialName("reviewText") val reviewText: String?,
    @SerialName("bookId") val bookId: String,
    @SerialName("bookAuthorId") val bookAuthorId: String,
    @SerialName("userId") val userId: Int,
    @SerialName("userName") val userName: String,
    @SerialName("likesCount") val likesCount: Int,
    @SerialName("dislikesCount") val dislikesCount: Int,
    @SerialName("answersCount") val answersCount: Int,
    @SerialName("isApprovedReview") val isApprovedReview: Boolean,
    @SerialName("isDisapprovedReview") val isDisapprovedReview: Boolean,
    @SerialName("timestampOfCreatingScore") val timestampOfCreatingScore: Long,
    @SerialName("timestampOfUpdatingScore") val timestampOfUpdatingScore: Long,
    @SerialName("timestampOfCreatingReview") val timestampOfCreatingReview: Long,
    @SerialName("timestampOfUpdatingReview") val timestampOfUpdatingReview: Long,
    @SerialName("bookGenreId") val bookGenreId: Int,
    @SerialName("isCreatedManuallyBook") val isCreatedManuallyBook: Boolean,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean,
)

fun ReviewAndRatingEntity.toVo(): ReviewAndRatingVo {
    return ReviewAndRatingVo(
        id = id,
        localId = localId?.toInt(),
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
        updatedByDeviceId = null,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
    )
}

fun ReviewAndRatingVo.toLocalDto(): ReviewAndRatingEntity {
    return ReviewAndRatingEntity(
        id = id,
        localId = localId?.toLong(),
        ratingScore = ratingScore,
        reviewText = reviewText,
        bookId = bookId,
        bookAuthorId = bookAuthorId,
        userId = userId,
        userName = userName,
        likesCount = likesCount ?: 0,
        dislikesCount = dislikesCount ?: 0,
        answersCount = answersCount ?: 0,
        isApprovedReview = isApprovedReview,
        isDisapprovedReview = isDisapprovedReview,
        timestampOfCreatingScore = timestampOfCreatingScore,
        timestampOfUpdatingScore = timestampOfUpdatingScore,
        timestampOfCreatingReview = timestampOfCreatingReview,
        timestampOfUpdatingReview = timestampOfUpdatingReview,
        bookGenreId = bookGenreId,
        isCreatedManuallyBook = isCreatedManuallyBook,
        bookForAllUsers = bookForAllUsers,
    )
}