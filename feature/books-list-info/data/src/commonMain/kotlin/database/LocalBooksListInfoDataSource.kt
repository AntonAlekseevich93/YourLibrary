package database

import database.room.RoomMainDataSource
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity
import kotlinx.coroutines.flow.Flow
import platform.PlatformInfoData

class LocalBooksListInfoDataSource(
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

    suspend fun getLocalBookByLocalId(bookLocalId: Long, userId: Long): Flow<List<BookEntity>> =
        booksDao.getLocalBookByLocalId(bookLocalId = bookLocalId, userId = userId)

    suspend fun getLocalBookById(bookId: String, userId: Long): Flow<List<BookEntity>> =
        booksDao.getLocalBookById(bookId = bookId, userId = userId)

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