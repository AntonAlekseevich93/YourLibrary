package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import database.room.entities.ReviewAndRatingTimestampEntity

@Dao
interface ReviewAndRatingTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTimestamp(timestampDto: ReviewAndRatingTimestampEntity)

}