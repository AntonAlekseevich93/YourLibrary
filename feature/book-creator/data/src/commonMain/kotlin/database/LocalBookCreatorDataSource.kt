package database

import database.room.RoomMainDataSource
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity
import platform.PlatformInfoData

class LocalBookCreatorDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksDao = roomDb.booksDao
    private val bookTimestampDao = roomDb.bookTimestampDao

    suspend fun getBookTimestamp(userId: Long) = bookTimestampDao.getTimestamp(userId).firstOrNull()

    suspend fun updateBookTimestamp(bookTimestamp: BookTimestampEntity) {
        bookTimestampDao.updateTimestamp(bookTimestamp)
    }

    suspend fun createBook(book: BookEntity): BookEntity {
        val time = platformInfo.getCurrentTime().timeInMillis
        booksDao.insertBook(
            book.copy(
                timestampOfCreating = time,
                timestampOfUpdating = time,
            )
        )
        return booksDao.getBookByRoomId(book.bookId).first()
    }

    suspend fun updateBook(book: BookEntity) {
        booksDao.updateBook(book)
    }

    suspend fun getBookStatusByBookId(bookId: String): String? =
        booksDao.getBookStatusByBookId(bookId).firstOrNull()?.readingStatus
}