package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.BookVo
import main_models.ReadingStatusUtils

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val roomId: Long?,
    val bookId: String,
    val serverId: Int?,
    val originalAuthorId: String,
    val bookName: String,
    val originalAuthorName: String,
    val description: String,
    val userCoverUrl: String?,
    val pageCount: Int,
    val isbn: String?,
    val readingStatus: String,
    val ageRestrictions: String?,
    val bookGenreId: Int,
    val bookGenreName: String,
    val startDateInString: String?,
    val endDateInString: String?,
    val startDateInMillis: Long?,
    val endDateInMillis: Long?,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val isRussian: Boolean?,
    val imageName: String?,
)

fun BookVo.toLocalDto(): BookEntity = BookEntity(
    roomId = roomId,
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
    bookGenreName = bookGenreName,
    startDateInString = startDateInString,
    endDateInString = endDateInString,
    startDateInMillis = startDateInMillis,
    endDateInMillis = endDateInMillis,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
    isRussian = isRussian,
    imageName = imageName
)

fun BookEntity.toVo(): BookVo =
    BookVo(
        roomId = roomId,
        bookId = bookId,
        serverId = serverId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
        originalAuthorName = originalAuthorName,
        description = description,
        coverUrl = null, //todo добавлять префикс к названию картинки
        userCoverUrl = userCoverUrl,
        pageCount = pageCount,
        isbn = isbn,
        readingStatus = ReadingStatusUtils.textToReadingStatus(readingStatus),
        ageRestrictions = ageRestrictions,
        bookGenreId = bookGenreId,
        bookGenreName = bookGenreName,
        startDateInString = startDateInString,
        endDateInString = endDateInString,
        startDateInMillis = startDateInMillis,
        endDateInMillis = endDateInMillis,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        isRussian = isRussian,
        imageName = imageName
    )