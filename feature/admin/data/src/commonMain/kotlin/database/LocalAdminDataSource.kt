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
    private val serviceDevelopmentBookTimestampDao = roomDb.userServiceDevelopmentBookTimestampDao
    private val serviceDevelopmentBookDao = roomDb.userServiceDevelopmentBookDao
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
        serviceDevelopmentBookTimestampDao.deleteAllData()
        serviceDevelopmentBookDao.deleteAllData()
    }

}