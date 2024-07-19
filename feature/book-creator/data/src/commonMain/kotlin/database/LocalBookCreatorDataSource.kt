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

    suspend fun getBookTimestamp(userId: Long): BookTimestampEntity {
        val timestamp = bookTimestampDao.getTimestamp(userId).firstOrNull()
        return timestamp ?: createEmptyTimestamp(userId)
    }

    suspend fun updateBookTimestamp(bookTimestamp: BookTimestampEntity) {
        bookTimestampDao.insertOrUpdateTimestamp(bookTimestamp)
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