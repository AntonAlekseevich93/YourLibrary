package database

import database.room.RoomMainDataSource
import database.room.entities.BookEntity
import kotlinx.coroutines.flow.Flow
import platform.PlatformInfoData

class LocalShelfDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksDao = roomDb.booksDao
    private val bookTimestampDao = roomDb.bookTimestampDao

    suspend fun getAllBooks(readingStatus: String, userId: Int): Flow<List<BookEntity>> =
        booksDao.getAllBooksFlowByStatus(readingStatus, userId = userId)


}