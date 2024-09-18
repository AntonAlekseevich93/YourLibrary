package main_models.rest.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.BookVo
import main_models.ReadingStatusUtils
import main_models.rest.authors.AuthorResponse

@Serializable
data class UserBookResponse(
    @SerialName("book") val book: UserBookRemoteDto? = null,
    @SerialName("author") val author: AuthorResponse? = null,
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
    @SerialName("isRussian") val isRussian: Boolean? = null,
    @SerialName("imageName") val imageName: String? = null,
    @SerialName("startDateInString") val startDateInString: String? = null,
    @SerialName("endDateInString") val endDateInString: String? = null,
    @SerialName("startDateInMillis") val startDateInMillis: Long? = null,
    @SerialName("endDateInMillis") val endDateInMillis: Long? = null,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long? = null,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("authorIsCreatedManually") val authorIsCreatedManually: Boolean? = null,
    @SerialName("isCreatedManually") val isCreatedManually: Boolean? = null,
    @SerialName("imageFolderId") val imageFolderId: Int? = null,
    @SerialName("ratingValue") val ratingValue: Double,
    @SerialName("ratingCount") val ratingCount: Int,
    @SerialName("reviewCount") val reviewCount: Int,
    @SerialName("ratingSum") val ratingSum: Int,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean,
    @SerialName("originalMainBookId") val originalMainBookId: String,
    @SerialName("lang") val lang: String,
    @SerialName("publicationYear") val publicationYear: String?,
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
    isRussian = isRussian,
    imageName = imageName,
    startDateInString = startDateInString,
    endDateInString = endDateInString,
    startDateInMillis = startDateInMillis,
    endDateInMillis = endDateInMillis,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
    authorIsCreatedManually = authorIsCreatedManually,
    isCreatedManually = bookIsCreatedManually,
    imageFolderId = imageFolderId,
    ratingValue = ratingValue,
    ratingCount = ratingCount,
    reviewCount = reviewCount,
    ratingSum = ratingSum,
    bookForAllUsers = bookForAllUsers,
    originalMainBookId = originalMainBookId,
    lang = lang,
    publicationYear = publicationYear,
)

fun UserBookRemoteDto.toVo(): BookVo? {
    return BookVo(
        serverId = id,
        bookId = bookId ?: return null,
        localId = null,
        originalAuthorId = originalAuthorId ?: return null,
        originalAuthorName = originalAuthorName ?: return null,
        bookName = bookName ?: return null,
        bookNameUppercase = bookName.uppercase(),
        description = description ?: return null,
        userCoverUrl = userCoverUrl,
        pageCount = pageCount ?: return null,
        isbn = isbn,
        readingStatus = readingStatus?.let { ReadingStatusUtils.textToReadingStatus(it) }
            ?: return null,
        ageRestrictions = ageRestrictions,
        bookGenreId = bookGenreId ?: return null,
        isRussian = isRussian,
        imageName = imageName,
        startDateInString = startDateInString,
        endDateInString = endDateInString,
        startDateInMillis = startDateInMillis,
        endDateInMillis = endDateInMillis,
        timestampOfCreating = timestampOfCreating ?: return null,
        timestampOfUpdating = timestampOfUpdating ?: return null,
        authorIsCreatedManually = authorIsCreatedManually ?: return null,
        isLoadedToServer = true,
        bookIsCreatedManually = isCreatedManually ?: return null,
        imageFolderId = imageFolderId,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        reviewCount = reviewCount,
        ratingSum = ratingSum,
        bookForAllUsers = bookForAllUsers,
        originalMainBookId = originalMainBookId,
        lang = lang,
        publicationYear = publicationYear,
    )
}