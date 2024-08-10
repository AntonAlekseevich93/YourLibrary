package main_models.rating_review

data class ReviewAndRatingTimestampVo(
    val userId: Long,
    val otherDevicesTimestampRating: Long,
    val thisDeviceTimestampRating: Long,
    val otherDevicesTimestampReview: Long,
    val thisDeviceTimestampReview: Long,
)
