package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import database.room.entities.ReviewAndRatingEntity

@Dao
interface ReviewAndRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReviewAndRating(reviewAndRating: ReviewAndRatingEntity)
}