package main_models.rest.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.authors.AuthorResponse

@Serializable
data class BooksWithAuthorsResponse(
    @SerialName("booksCurrentDevice") val booksCurrentDevice: List<UserBookRemoteDto>,
    @SerialName("booksOtherDevices") val booksOtherDevices: List<UserBookRemoteDto>,
    @SerialName("authorsCurrentDevice") val authorsCurrentDevice: List<AuthorResponse>,
    @SerialName("authorsOtherDevices") val authorsOtherDevices: List<AuthorResponse>,
)