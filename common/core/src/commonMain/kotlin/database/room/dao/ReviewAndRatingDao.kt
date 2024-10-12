package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.ReviewAndRatingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewAndRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReviewAndRating(reviewAndRating: ReviewAndRatingEntity): Long

    @Query("DELETE FROM ReviewAndRatingEntity")
    suspend fun deleteAllData()

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE (timestampOfUpdatingScore > :ratingTimestamp OR timestampOfUpdatingReview > :reviewTimestamp) AND userId = :userId")
    suspend fun getNotSynchronizedReviewAndRating(
        ratingTimestamp: Long,
        reviewTimestamp: Long,
        userId: Int
    ): List<ReviewAndRatingEntity>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId and mainBookId = :mainBookId")
    suspend fun getReviewAndRatingByBookId(
        mainBookId: String,
        userId: Int
    ): List<ReviewAndRatingEntity>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId and mainBookId = :mainBookId")
    fun getCurrentUserReviewAndRatingByBookFlow(
        mainBookId: String,
        userId: Int
    ): Flow<List<ReviewAndRatingEntity>>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId and mainBookId = :mainBookId")
    suspend fun getCurrentUserReviewAndRatingByBook(
        mainBookId: String,
        userId: Int
    ): List<ReviewAndRatingEntity>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId")
    fun getCurrentUserAllReviewsAndRatings(
        userId: Int
    ): Flow<List<ReviewAndRatingEntity>>
}