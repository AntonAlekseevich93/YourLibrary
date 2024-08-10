package database

import database.room.RoomMainDataSource
import platform.PlatformInfoData

class LocalReviewAndRatingDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val reviewAndRatingDao = roomDb.reviewAndRatingDao
    private val reviewAndRatingTimestampDao = roomDb.reviewAndRatingTimestampDao

//    suspend fun getBookTimestamp(userId: Long) =
//        reviewAndRatingTimestampDao.getTimestamp(userId).firstOrNull() ?: createEmptyTimestamp(userId)

//    suspend fun updateBookTimestamp(bookTimestamp: BookTimestampEntity) {
//        bookTimestampDao.insertOrUpdateTimestamp(bookTimestamp)
//    }

}