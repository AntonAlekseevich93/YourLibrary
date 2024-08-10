package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.rating_review.ReviewAndRatingTimestampVo

@Entity
data class ReviewAndRatingTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Long,
    val otherDevicesTimestampRating: Long,
    val thisDeviceTimestampRating: Long,
    val otherDevicesTimestampReview: Long,
    val thisDeviceTimestampReview: Long,
)

fun ReviewAndRatingTimestampEntity.toVo() = ReviewAndRatingTimestampVo(
    userId = userId,
    otherDevicesTimestampRating = otherDevicesTimestampRating,
    thisDeviceTimestampRating = thisDeviceTimestampRating,
    otherDevicesTimestampReview = otherDevicesTimestampReview,
    thisDeviceTimestampReview = thisDeviceTimestampReview,
)