package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SynchronizeUserDataRequest(
    @SerialName("booksWithAuthors") val booksWithAuthors: SynchronizeBooksWithAuthorsRequest?,
    @SerialName("reviewsAndRatings") val reviewsAndRatings: SynchronizeReviewAndRatingRequest?,
    @SerialName("serviceDevelopment") val serviceDevelopment: SynchronizeServiceDevelopmentRequest?,
    @SerialName("user_info") val userInfo: SynchronizeUserInfoRequest?,
)

@Serializable
data class SynchronizeUserDataContent(
    @SerialName("books_with_authors_content")
    val booksWithAuthorsContent: SynchronizeBooksWithAuthorsResponse? = null,
    @SerialName("reviews_and_ratings_content")
    val reviewAndRatingResponse: SynchronizeReviewAndRatingContentResponse? = null,
    @SerialName("service_development_content")
    val serviceDevelopmentResponse: SynchronizeServiceDevelopmentContentResponse? = null,
    @SerialName("user_info_content")
    val userInfoResponse: SynchronizeUserContentResponse? = null,
)