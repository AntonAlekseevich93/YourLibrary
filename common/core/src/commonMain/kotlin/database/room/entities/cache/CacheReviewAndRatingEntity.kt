package database.room.entities.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.rest.rating_review.CacheReviewAndRatingVo
import main_models.rest.rating_review.ReviewAndRatingRemoteDto

@Entity
data class CacheReviewAndRatingEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("cacheLocalId") val localId: Long? = null,
    @SerialName("cacheReviewAndRating")
    @Embedded
    val cacheReviewAndRating: ReviewAndRatingRemoteDto?,
    @SerialName("cacheMainBookId") val cacheMainBookId: String,
    @SerialName("cacheTimestamp") val cacheTimestamp: Long,
    @SerialName("cacheUserId") val cacheUserId: Int,
)

fun CacheReviewAndRatingEntity.toCacheVo(): CacheReviewAndRatingVo =
    CacheReviewAndRatingVo(
        localId = localId,
        cacheReviewAndRating = cacheReviewAndRating,
        cacheMainBookId = cacheMainBookId,
        cacheTimestamp = cacheTimestamp,
        cacheUserId = cacheUserId
    )
