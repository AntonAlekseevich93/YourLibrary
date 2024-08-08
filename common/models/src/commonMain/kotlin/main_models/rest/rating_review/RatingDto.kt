package main_models.rest.rating_review

import kotlinx.serialization.SerialName

class RatingDto(
    @SerialName("rating") val rating: Int,
    @SerialName("bookId") val bookId: String,
    @SerialName("bookAuthorId") val bookAuthorId: String,
    @SerialName("bookGenreId") val bookGenreId: Int,
    @SerialName("isCreatedManuallyBook") val isCreatedManuallyBook: Boolean,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean
)