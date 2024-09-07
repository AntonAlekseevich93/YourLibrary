package database

import database.room.RoomMainDataSource

class LocalAdminDataSource(
    roomDb: RoomMainDataSource,
) {
    private val reviewAndRatingDao = roomDb.reviewAndRatingDao
    suspend fun clearReviewAndRatingDb() {
        reviewAndRatingDao.deleteAllData()
    }

}