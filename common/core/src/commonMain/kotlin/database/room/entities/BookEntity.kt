package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.BookVo
import main_models.ReadingStatusUtils

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false)
    @SerialName("bookId") val bookId: String,
    @SerialName("serverId") val serverId: Int?,
    @SerialName("originalAuthorId") val originalAuthorId: String,
    @SerialName("bookName") val bookName: String,
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
)

fun BookVo.toLocalDto(userId: Long): BookEntity = BookEntity(
    bookId = bookId,
    serverId = serverId,
    originalAuthorId = originalAuthorId,
    bookName = bookName,
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
    imageFolderId = imageFolderId
)

fun BookEntity.toVo(): BookVo =
    BookVo(
        bookId = bookId,
        serverId = serverId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
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
        imageFolderId = imageFolderId
    )