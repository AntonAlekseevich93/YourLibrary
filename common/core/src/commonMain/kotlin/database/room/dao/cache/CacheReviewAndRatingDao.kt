package database.room.dao.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import database.room.entities.cache.CacheReviewAndRatingEntity

@Dao
interface CacheReviewAndRatingDao {
    @Insert
    suspend fun insertReviewAndRating(reviewAndRating: CacheReviewAndRatingEntity)

    @Query("SELECT * FROM CacheReviewAndRatingEntity WHERE cacheMainBookId = :mainBookId and cacheUserId =:userId")
    suspend fun getCacheReviewAndRatingByBook(
        mainBookId: String,
        userId: Int,
    ): List<CacheReviewAndRatingEntity>

    @Query("DELETE FROM CacheReviewAndRatingEntity WHERE cacheMainBookId =:mainBookId and cacheUserId =:userId")
    suspend fun deleteCacheReviewAndRatingByBook(
        mainBookId: String,
        userId: Int
    )

    @Query("DELETE FROM CacheReviewAndRatingEntity")
    suspend fun clearAllCache()

}