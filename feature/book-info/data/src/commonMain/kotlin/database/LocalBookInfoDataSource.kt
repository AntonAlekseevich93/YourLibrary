package database

import database.room.RoomMainDataSource
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity
import platform.PlatformInfoData

class LocalBookInfoDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksDao = roomDb.booksDao
    private val bookTimestampDao = roomDb.bookTimestampDao

    suspend fun getBookTimestamp(userId: Long) =
        bookTimestampDao.getTimestamp(userId).firstOrNull() ?: createEmptyTimestamp(userId)

    suspend fun updateBookTimestamp(bookTimestamp: BookTimestampEntity) {
        bookTimestampDao.insertOrUpdateTimestamp(bookTimestamp)
    }

    suspend fun addOrUpdateBooks(books: List<BookEntity>, userId: Long) {
        books.forEach { book ->
            val existedBook = booksDao.getBookByBookId(book.bookId, userId = userId).firstOrNull()
            if (existedBook == null) {
                booksDao.insertBook(book)
            } else {
                booksDao.updateBook(book.copy(localId = existedBook.localId))
            }
        }
    }

    suspend fun getNotSynchronizedBooks(userId: Long): List<BookEntity> {
        val timestamp = getBookTimestamp(userId)
        return booksDao.getNotSynchronizedBooks(timestamp.thisDeviceTimestamp, userId = userId)
    }

    private suspend fun createEmptyTimestamp(userId: Long): BookTimestampEntity {
        val timestamp = BookTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0
        )
        bookTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }


}