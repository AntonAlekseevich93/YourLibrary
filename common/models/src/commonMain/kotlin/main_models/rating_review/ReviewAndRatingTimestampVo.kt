package main_models.rating_review

data class ReviewAndRatingTimestampVo(
    val userId: Int,
    val otherDevicesTimestampRating: Long,
    val thisDeviceTimestampRating: Long,
    val otherDevicesTimestampReview: Long,
    val thisDeviceTimestampReview: Long,
)
