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

    suspend fun createBook(book: BookEntity, userId: Long): BookEntity {
        val time = platformInfo.getCurrentTime().timeInMillis
        val existedBook = booksDao.getBookByBookId(book.bookId, userId = userId).firstOrNull()
        if (existedBook == null) {
            booksDao.insertBook(
                book.copy(
                    timestampOfCreating = time,
                    timestampOfUpdating = time,
                )
            )
        } else {
            booksDao.updateBook(
                book.copy(
                    localId = existedBook.localId,
                    timestampOfCreating = time,
                    timestampOfUpdating = time,
                )
            )
        }
        return booksDao.getBookByBookId(book.bookId, userId = userId).first()
    }

    suspend fun updateBookWithoutUpdateTime(book: BookEntity, userId: Long) {
        val localId = booksDao.getBookByBookId(book.bookId, userId = userId).firstOrNull()?.localId
        if (localId != null) {
            booksDao.updateBook(book.copy(localId = localId))
        } else {
            booksDao.insertBook(book)
        }
    }

    suspend fun getBookStatusByBookId(bookId: String, userId: Long): String? =
        booksDao.getBookStatusByBookId(bookId, userId = userId).firstOrNull()?.readingStatus

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