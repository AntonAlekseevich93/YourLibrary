package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.authors.AuthorResponse
import main_models.rest.books.UserBookRemoteDto

@Serializable
data class SynchronizeBooksWithAuthorsRequest(
    @SerialName("books_device_last_timestamp") val booksThisDeviceTimestamp: Long,
    @SerialName("books_other_devices_last_timestamp") val booksOtherDevicesTimestamp: Long,
    @SerialName("authors_device_last_timestamp") val authorsThisDeviceTimestamp: Long,
    @SerialName("authors_other_devices_last_timestamp") val authorsOtherDevicesTimestamp: Long,
    @SerialName("books") val books: List<UserBookRemoteDto>,
)

@Serializable
data class SynchronizeBooksWithAuthorsResponse(
    @SerialName("missing_books_and_authors_from_server")
    val missingBooksAndAuthorsFromServer: MissingBooksAndAuthorsFromServer?,
    @SerialName("current_device_books_and_authors_added_to_server")
    val currentDeviceBooksAndAuthorsAddedToServer: CurrentDeviceBooksAndAuthorsAddedToServer?,
    @SerialName("currentDeviceBookLastTimestamp") val currentDeviceBookLastTimestamp: Long?,
    @SerialName("currentDeviceAuthorLastTimestamp") val currentDeviceAuthorLastTimestamp: Long?,
)

@Serializable
data class MissingBooksAndAuthorsFromServer(
    @SerialName("booksCurrentDevice") val booksCurrentDevice: List<UserBookRemoteDto>?,
    @SerialName("booksOtherDevices") val booksOtherDevices: List<UserBookRemoteDto>?,
    @SerialName("authorsCurrentDevice") val authorsCurrentDevice: List<AuthorResponse>?,
    @SerialName("authorsOtherDevices") val authorsOtherDevices: List<AuthorResponse>?,
)

@Serializable
data class CurrentDeviceBooksAndAuthorsAddedToServer(
    @SerialName("books") val books: List<UserBookRemoteDto>?,
    @SerialName("authors") val authors: List<AuthorResponse>?,
)