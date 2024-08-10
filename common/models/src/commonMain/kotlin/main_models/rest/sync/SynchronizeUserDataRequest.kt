package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SynchronizeUserDataRequest(
    @SerialName("booksWithAuthors") val booksWithAuthors: SynchronizeBooksWithAuthorsRequest?,
    @SerialName("reviewsAndRatings") val reviewsAndRatings: SynchronizeReviewAndRatingRequest?,
)

@Serializable
data class SynchronizeUserDataContent(
    @SerialName("books_with_authors_content")
    val booksWithAuthorsContent: SynchronizeBooksWithAuthorsResponse? = null,
    @SerialName("reviews_and_ratings_content")
    val reviewAndRatingContent: SynchronizeReviewAndRatingContent? = null,
)