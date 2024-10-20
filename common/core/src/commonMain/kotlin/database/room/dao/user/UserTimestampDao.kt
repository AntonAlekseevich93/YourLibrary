package database.room.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.user.UserTimestampEntity

@Dao
interface UserTimestampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTimestamp(timestampDto: UserTimestampEntity)

    @Query("SELECT * FROM UserTimestampEntity WHERE userId = :userId")
    suspend fun getTimestamp(userId: Int): List<UserTimestampEntity>
}