package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity
data class ReviewAndRatingEntity(
    @PrimaryKey(autoGenerate = true) @SerialName("localId") val localId: Long? = null,
    @SerialName("remoteId") val id: Int?,
    @SerialName("ratingScore") val ratingScore: Int,
    @SerialName("reviewText") val reviewText: String?,
    @SerialName("bookId") val bookId: String,
    @SerialName("bookAuthorId") val bookAuthorId: String,
    @SerialName("userId") val userId: Int?,
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