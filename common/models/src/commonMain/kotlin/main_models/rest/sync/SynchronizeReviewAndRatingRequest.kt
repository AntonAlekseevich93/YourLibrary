package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.rating_review.ReviewAndRatingDto

@Serializable
data class SynchronizeReviewAndRatingRequest(
    @SerialName("review_device_last_timestamp") val reviewThisDeviceTimestamp: Long,
    @SerialName("review_other_devices_last_timestamp") val reviewOtherDevicesTimestamp: Long,
    @SerialName("rating_device_last_timestamp") val ratingThisDeviceTimestamp: Long,
    @SerialName("rating_other_devices_last_timestamp") val ratingOtherDevicesTimestamp: Long,
    @SerialName("reviews_and_ratings") val reviewAndRatings: List<ReviewAndRatingDto>,
)

@Serializable
data class SynchronizeReviewAndRatingContent(
    @SerialName("missingReviewsAndRatingsFromServer")
    val missingReviewsAndRatingsFromServer: MissingReviewsAndRatingsFromServer? = null,
    @SerialName("currentDeviceReviewsAndRatingsAddedToServer")
    val currentDeviceReviewsAndRatingsAddedToServer: CurrentDeviceReviewsAndRatingsAddedToServer? = null,
)

@Serializable
data class MissingReviewsAndRatingsFromServer(
    @SerialName("reviewsAndRatingCurrentDevice") val reviewsAndRatingCurrentDevice: List<ReviewAndRatingDto>,
    @SerialName("reviewsAndRatingOtherDevices") val reviewsAndRatingOtherDevices: List<ReviewAndRatingDto>,
)

@Serializable
data class CurrentDeviceReviewsAndRatingsAddedToServer(
    @SerialName("reviewsAndRating") val reviewsAndRating: List<ReviewAndRatingDto>,
)