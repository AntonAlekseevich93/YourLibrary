package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.AuthorsTimestampEntity

@Dao
interface AuthorsTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTimestamp(timestampDto: AuthorsTimestampEntity)

    @Query("SELECT * FROM AUTHORSTIMESTAMPENTITY WHERE userId = :userId")
    suspend fun getTimestamp(userId: Long): List<AuthorsTimestampEntity>
}