package database

import database.room.RoomMainDataSource
import database.room.entities.cache.CacheBookByAuthorEntity
import database.room.entities.cache.CacheReviewAndRatingEntity
import platform.PlatformInfoData
import java.util.concurrent.TimeUnit

class CacheManagerDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksByAuthorDao = roomDb.cacheBooksByAuthorDao
    private val reviewAndRatingDao = roomDb.cacheReviewAndRatingDao
    private val _currentTimestamp
        get() = platformInfo.getCurrentTime().timeInMillis

    suspend fun getCacheAllAuthorBooks(
        authorId: String,
        userId: Long
    ): List<CacheBookByAuthorEntity> {
        val cachedBooks = booksByAuthorDao.getCacheBooksByAuthor(
            authorId = authorId,
            userId = userId
        )
        return if (cachedBooks.isEmpty()) emptyList()
        else {
            val cachedTimestamp = cachedBooks.firstOrNull()?.cacheTimestamp ?: return emptyList()
            if (isCacheValid(cachedTimestamp)) {
                return cachedBooks
            } else {
                booksByAuthorDao.deleteCacheByAuthor(authorId, userId)
            }
            emptyList()
        }
    }

    suspend fun saveAllAuthorsBooks(
        authorId: String,
        userId: Long,
        books: List<CacheBookByAuthorEntity>
    ) {
        booksByAuthorDao.deleteCacheByAuthor(authorId, userId)
        books.forEach {
            booksByAuthorDao.insertBook(it)
        }
    }

    suspend fun getCacheAllReviewsAndRatingsByBook(
        mainBookId: String,
        userId: Long
    ): List<CacheReviewAndRatingEntity> {
        val cachedReviewAndRating = reviewAndRatingDao.getCacheReviewAndRatingByBook(
            mainBookId = mainBookId,
            userId = userId
        )
        return if (cachedReviewAndRating.isEmpty()) emptyList()
        else {
            val cachedTimestamp =
                cachedReviewAndRating.firstOrNull()?.cacheTimestamp ?: return emptyList()
            if (isCacheValid(cachedTimestamp)) {
                return cachedReviewAndRating
            } else {
                reviewAndRatingDao.deleteCacheReviewAndRatingByBook(mainBookId, userId)
            }
            emptyList()
        }
    }

    suspend fun saveAllReviewAndRatingByBook(
        mainBookId: String,
        userId: Long,
        reviewsAndRatings: List<CacheReviewAndRatingEntity>
    ) {
        reviewAndRatingDao.deleteCacheReviewAndRatingByBook(mainBookId, userId)
        reviewsAndRatings.forEach {
            reviewAndRatingDao.insertReviewAndRating(it)
        }
    }

    suspend fun clearAllCache() {
        booksByAuthorDao.clearAllCache()
        reviewAndRatingDao.clearAllCache()
    }

    private fun isCacheValid(cachedTimestamp: Long): Boolean {
        val currentTimestamp = _currentTimestamp
        val differenceInMillis = currentTimestamp - cachedTimestamp
        val differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)
        return differenceInMinutes < HOURS_24
    }

    companion object {
        private const val HOURS_24 = 1440 //1440 минут (24 часа)
    }


}