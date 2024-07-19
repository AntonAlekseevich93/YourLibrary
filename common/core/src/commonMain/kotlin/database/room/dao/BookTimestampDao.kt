package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.BookTimestampEntity

@Dao
interface BookTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTimestamp(timestampDto: BookTimestampEntity)

    @Query("SELECT * FROM BOOKTIMESTAMPENTITY WHERE userId = :userId")
    suspend fun getTimestamp(userId: Long): List<BookTimestampEntity>
}