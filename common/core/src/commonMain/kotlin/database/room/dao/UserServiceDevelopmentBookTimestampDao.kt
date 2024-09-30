package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.UserServiceDevelopmentBookTimestampEntity

@Dao
interface UserServiceDevelopmentBookTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTimestamp(timestampDto: UserServiceDevelopmentBookTimestampEntity)

    @Query("SELECT * FROM UserServiceDevelopmentBookTimestampEntity WHERE userId = :userId")
    suspend fun getTimestamp(userId: Int): List<UserServiceDevelopmentBookTimestampEntity>

    @Query("DELETE FROM UserServiceDevelopmentBookTimestampEntity")
    suspend fun deleteAllData()
}