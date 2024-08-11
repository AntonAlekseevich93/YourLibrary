package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.rating_review.ReviewAndRatingRemoteDto

@Serializable
data class SynchronizeReviewAndRatingRequest(
    @SerialName("review_device_last_timestamp") val reviewThisDeviceTimestamp: Long,
    @SerialName("review_other_devices_last_timestamp") val reviewOtherDevicesTimestamp: Long,
    @SerialName("rating_device_last_timestamp") val ratingThisDeviceTimestamp: Long,
    @SerialName("rating_other_devices_last_timestamp") val ratingOtherDevicesTimestamp: Long,
    @SerialName("reviews_and_ratings") val reviewAndRatings: List<ReviewAndRatingRemoteDto>,
)

@Serializable
data class SynchronizeReviewAndRatingContent(
    @SerialName("missingReviewsAndRatingsFromServer")
    val missingReviewsAndRatingsFromServer: MissingReviewsAndRatingsFromServer? = null,
    @SerialName("currentDeviceReviewsAndRatingsAddedToServer")
    val currentDeviceReviewsAndRatingsAddedToServer: CurrentDeviceReviewsAndRatingsAddedToServer? = null,
    @SerialName("currentDeviceReviewLastTimestamp") val currentDeviceReviewLastTimestamp: Long?,
    @SerialName("currentDeviceRatingLastTimestamp") val currentDeviceRatingLastTimestamp: Long?,
)

@Serializable
data class MissingReviewsAndRatingsFromServer(
    @SerialName("reviewsAndRatingCurrentDevice") val reviewsAndRatingCurrentDevice: List<ReviewAndRatingRemoteDto>,
    @SerialName("reviewsAndRatingOtherDevices") val reviewsAndRatingOtherDevices: List<ReviewAndRatingRemoteDto>,
)

@Serializable
data class CurrentDeviceReviewsAndRatingsAddedToServer(
    @SerialName("reviewsAndRating") val reviewsAndRating: List<ReviewAndRatingRemoteDto>,
)