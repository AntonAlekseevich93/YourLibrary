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

    suspend fun getBookTimestamp(userId: Int): BookTimestampEntity {
        val timestamp = bookTimestampDao.getTimestamp(userId).firstOrNull()
        return timestamp ?: createEmptyTimestamp(userId)
    }

    suspend fun updateBookTimestamp(bookTimestamp: BookTimestampEntity) {
        bookTimestampDao.insertOrUpdateTimestamp(bookTimestamp)
    }

    suspend fun createBook(book: BookEntity, userId: Int): BookEntity {
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

    suspend fun updateBookWithoutUpdateTime(book: BookEntity, userId: Int) {
        val localId = booksDao.getBookByBookId(book.bookId, userId = userId).firstOrNull()?.localId
        if (localId != null) {
            booksDao.updateBook(book.copy(localId = localId))
        } else {
            booksDao.insertBook(book)
        }
    }

    suspend fun getBookStatusByBookId(bookId: String, userId: Int): String? =
        booksDao.getBookStatusByBookId(bookId, userId = userId).firstOrNull()?.readingStatus

    suspend fun getLocalBookById(bookId: String, userId: Int) =
        booksDao.getBookByBookId(bookId, userId = userId).firstOrNull()

    private suspend fun createEmptyTimestamp(userId: Int): BookTimestampEntity {
        val timestamp = BookTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0
        )
        bookTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }
}