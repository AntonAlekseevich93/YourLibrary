package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.BookVo
import main_models.ReadingStatusUtils

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = true) @SerialName("localId") val localId: Long? = null,
    @SerialName("bookId") val bookId: String,
    @SerialName("serverId") val serverId: Int?,
    @SerialName("originalAuthorId") val originalAuthorId: String,
    @SerialName("bookName") val bookName: String,
    @SerialName("bookNameUppercase") val bookNameUppercase: String,
    @SerialName("originalAuthorName") val originalAuthorName: String,
    @SerialName("userCoverUrl") val userCoverUrl: String?,
    @SerialName("pageCount") val pageCount: Int,
    @SerialName("isbn") val isbn: String?,
    @SerialName("readingStatus") val readingStatus: String,
    @SerialName("ageRestrictions") val ageRestrictions: String?,
    @SerialName("bookGenreId") val bookGenreId: Int,
    @SerialName("startDateInString") val startDateInString: String?,
    @SerialName("endDateInString") val endDateInString: String?,
    @SerialName("startDateInMillis") val startDateInMillis: Long?,
    @SerialName("endDateInMillis") val endDateInMillis: Long?,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long,
    @SerialName("isRussian") val isRussian: Boolean?,
    @SerialName("imageName") val imageName: String?,
    @SerialName("description") val description: String,
    @SerialName("userId") val userId: Long,
    @SerialName("authorIsCreatedManually") val authorIsCreatedManually: Boolean,
    @SerialName("isLoadedToServer") val isLoadedToServer: Boolean,
    @SerialName("bookIsCreatedManually") val bookIsCreatedManually: Boolean,
    @SerialName("imageFolderId") val imageFolderId: Int?,
    @SerialName("ratingValue") val ratingValue: Double,
    @SerialName("ratingCount") val ratingCount: Int,
    @SerialName("reviewCount") val reviewCount: Int,
    @SerialName("ratingSum") val ratingSum: Int,
    @SerialName("bookForAllUsers") val bookForAllUsers: Boolean,
)

fun BookVo.toLocalDto(userId: Long): BookEntity = BookEntity(
    bookId = bookId,
    serverId = serverId,
    localId = localId,
    originalAuthorId = originalAuthorId,
    bookName = bookName,
    bookNameUppercase = bookNameUppercase,
    originalAuthorName = originalAuthorName,
    description = description,
    userCoverUrl = userCoverUrl,
    pageCount = pageCount,
    isbn = isbn,
    readingStatus = readingStatus.id,
    ageRestrictions = ageRestrictions,
    bookGenreId = bookGenreId,
    startDateInString = startDateInString,
    endDateInString = endDateInString,
    startDateInMillis = startDateInMillis,
    endDateInMillis = endDateInMillis,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
    isRussian = isRussian,
    imageName = imageName,
    userId = userId,
    authorIsCreatedManually = authorIsCreatedManually,
    isLoadedToServer = isLoadedToServer,
    bookIsCreatedManually = bookIsCreatedManually,
    imageFolderId = imageFolderId,
    ratingValue = ratingValue,
    ratingCount = ratingCount,
    reviewCount = reviewCount,
    ratingSum = ratingSum,
    bookForAllUsers = bookForAllUsers,
)

fun BookEntity.toVo(remoteImageLink: String?): BookVo {
    val book = BookVo(
        bookId = bookId,
        serverId = serverId,
        localId = localId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
        bookNameUppercase = bookNameUppercase,
        originalAuthorName = originalAuthorName,
        description = description,
        userCoverUrl = userCoverUrl,
        pageCount = pageCount,
        isbn = isbn,
        readingStatus = ReadingStatusUtils.textToReadingStatus(readingStatus),
        ageRestrictions = ageRestrictions,
        bookGenreId = bookGenreId,
        startDateInString = startDateInString,
        endDateInString = endDateInString,
        startDateInMillis = startDateInMillis,
        endDateInMillis = endDateInMillis,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        isRussian = isRussian,
        imageName = imageName,
        authorIsCreatedManually = authorIsCreatedManually,
        isLoadedToServer = isLoadedToServer,
        bookIsCreatedManually = bookIsCreatedManually,
        imageFolderId = imageFolderId,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        reviewCount = reviewCount,
        ratingSum = ratingSum,
        bookForAllUsers = bookForAllUsers,
    )
    book.remoteImageLink = remoteImageLink
    return book
}