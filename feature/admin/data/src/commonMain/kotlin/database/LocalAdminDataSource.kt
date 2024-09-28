package database

import database.room.RoomMainDataSource

class LocalAdminDataSource(
    roomDb: RoomMainDataSource,
) {
    private val reviewAndRatingDao = roomDb.reviewAndRatingDao
    private val reviewAndRatingTimestampDao = roomDb.reviewAndRatingTimestampDao
    private val booksDao = roomDb.booksDao
    private val bookTimestampDao = roomDb.bookTimestampDao
    private val authorsDao = roomDb.authorsDao
    private val authorsTimestampDao = roomDb.authorsTimestampDao
    private val cacheBooksByAuthorDao = roomDb.cacheBooksByAuthorDao
    private val cacheReviewAndRatingDao = roomDb.cacheReviewAndRatingDao
    private val userNotificationsDao = roomDb.userNotificationsDao
    suspend fun clearReviewAndRatingDb() {
        reviewAndRatingDao.deleteAllData()
        reviewAndRatingTimestampDao.deleteAllData()
    }

    suspend fun clearAllDb() {
        clearReviewAndRatingDb()
        booksDao.deleteAllData()
        bookTimestampDao.deleteAllData()
        authorsDao.deleteAllData()
        authorsTimestampDao.deleteAllData()
        userNotificationsDao.deleteAllData()
        cacheBooksByAuthorDao.clearAllCache()
        cacheReviewAndRatingDao.clearAllCache()
    }

}