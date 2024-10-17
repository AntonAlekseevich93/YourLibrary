package database.room.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import database.room.entities.UserServiceDevelopmentBookEntity

@Dao
interface UserServiceDevelopmentBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserServiceDevelopmentBook(entity: UserServiceDevelopmentBookEntity)

    @Update
    suspend fun updateUserServiceDevelopmentBook(entity: UserServiceDevelopmentBookEntity)

    @Query("SELECT * FROM UserServiceDevelopmentBookEntity WHERE userBookId = :userBookId AND userId = :userId")
    suspend fun getUserServiceDevelopmentBook(
        userBookId: String,
        userId: Int
    ): List<UserServiceDevelopmentBookEntity>

    @Query("SELECT * FROM UserServiceDevelopmentBookEntity WHERE timestampOfUpdating > :timestamp AND userId = :userId")
    suspend fun getNotSynchronizedUserServiceDevelopmentBook(
        timestamp: Long,
        userId: Int
    ): List<UserServiceDevelopmentBookEntity>

    @Query("DELETE FROM UserServiceDevelopmentBookEntity")
    suspend fun deleteAllData()
}