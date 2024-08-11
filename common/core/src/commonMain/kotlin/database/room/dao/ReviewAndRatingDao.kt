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
    suspend fun insertOrUpdateReviewAndRating(reviewAndRating: ReviewAndRatingEntity)

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE (timestampOfUpdatingScore > :ratingTimestamp OR timestampOfUpdatingReview > :reviewTimestamp) AND userId = :userId")
    suspend fun getNotSynchronizedReviewAndRating(
        ratingTimestamp: Long,
        reviewTimestamp: Long,
        userId: Long
    ): List<ReviewAndRatingEntity>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId and bookId = :bookId")
    suspend fun getReviewAndRatingByBookId(
        bookId: String,
        userId: Long
    ): List<ReviewAndRatingEntity>

    @Query("SELECT * FROM ReviewAndRatingEntity WHERE userId = :userId and bookId = :bookId")
    fun getCurrentUserReviewAndRatingByBook(
        bookId: String,
        userId: Long
    ): Flow<List<ReviewAndRatingEntity>>
}