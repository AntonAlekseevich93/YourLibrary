package main_models.rest.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.BookVo

@Serializable
data class UserBookResponse(
    @SerialName("book") val book: UserBookRemoteDto? = null,
    @SerialName("error") val error: String? = null,
)

@Serializable
data class UserBookRemoteDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("originalAuthorId") val originalAuthorId: String? = null,
    @SerialName("originalAuthorName") val originalAuthorName: String? = null,
    @SerialName("bookName") val bookName: String? = null,
    @SerialName("userCoverUrl") val userCoverUrl: String? = null,
    @SerialName("pageCount") val pageCount: Int? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("readingStatus") val readingStatus: String? = null,
    @SerialName("ageRestriction") val ageRestrictions: String? = null,
    @SerialName("bookGenreId") val bookGenreId: Int? = null,
    @SerialName("bookGenreName") val bookGenreName: String? = null,
    @SerialName("isRussian") val isRussian: Boolean? = null,
    @SerialName("imageName") val imageName: String? = null,
    @SerialName("startDateInString") val startDateInString: String? = null,
    @SerialName("endDateInString") val endDateInString: String? = null,
    @SerialName("startDateInMillis") val startDateInMillis: Long? = null,
    @SerialName("endDateInMillis") val endDateInMillis: Long? = null,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long? = null,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long? = null,
    @SerialName("description") val description: String? = null,
)

fun BookVo.toRemoteDto(): UserBookRemoteDto = UserBookRemoteDto(
    id = serverId,
    bookId = bookId,
    originalAuthorId = originalAuthorId,
    originalAuthorName = originalAuthorName,
    bookName = bookName,
    description = description,
    userCoverUrl = userCoverUrl,
    pageCount = pageCount,
    isbn = isbn,
    readingStatus = readingStatus.id,
    ageRestrictions = ageRestrictions,
    bookGenreId = bookGenreId,
    bookGenreName = bookGenreName,
    isRussian = isRussian,
    imageName = imageName,
    startDateInString = startDateInString,
    endDateInString = endDateInString,
    startDateInMillis = startDateInMillis,
    endDateInMillis = endDateInMillis,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
)