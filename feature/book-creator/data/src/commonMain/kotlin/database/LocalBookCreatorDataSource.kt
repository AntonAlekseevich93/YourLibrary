package database

import database.room.RoomMainDataSource
import database.room.entities.BookEntity
import main_models.local_models.BookItemLocalDto
import platform.PlatformInfoData

class LocalBookCreatorDataSource(
    private val db: SqlDelightDataSource,
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksDao = roomDb.booksDao

    @Deprecated("replaced by room db")
    suspend fun createBook(bookItem: BookItemLocalDto) {
        bookItem.apply {
            db.appQuery.addBook(
                id = id,
                originalAuthorId = originalAuthorId,
                modifiedAuthorId = modifiedAuthorId,
                statusId = statusId,
                shelfId = shelfId,
                bookName = bookName,
                originalAuthorName = originalAuthorName,
                modifiedAuthorName = modifiedAuthorName,
                description = description,
                coverUrl = coverUrl,
                coverUrlFromParsing = coverUrlFromParsing,
                numbersOfPages = numbersOfPages?.toLong(),
                isbn = isbn,
                quotes = quotes,
                readingStatus = readingStatus,
                startDateInString = startDateInString,
                endDateInString = endDateInString,
                startDateInMillis = startDateInMillis,
                endDateInMillis = endDateInMillis,
                timestampOfCreating = timestampOfCreating,
                timestampOfUpdating = timestampOfUpdating,
            )
        }
    }

    suspend fun createBook(book: BookEntity): BookEntity {
        val time = platformInfo.getCurrentTime().timeInMillis
        val id = booksDao.insertBook(
            book.copy(
                timestampOfCreating = time,
                timestampOfUpdating = time,
            )
        )
        return booksDao.getBookByRoomId(id).first()
    }
}