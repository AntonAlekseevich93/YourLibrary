package database.room.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import database.room.entities.user.UserEntity
import database.room.entities.user.UserWithGoals
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun getAuthorizedUser(): Flow<List<UserWithGoals>>

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUserByEmail(email: String): List<UserEntity>

    @Query("UPDATE UserEntity set isAuthorized = 0")
    suspend fun setAllAsUnauthorized()

    @Transaction
    @Query("SELECT * FROM UserEntity")
    fun getUsersWithGoals(): List<UserWithGoals>

    @Query("SELECT * FROM UserEntity WHERE timestampOfUpdating > :timestamp AND userId = :userId")
    suspend fun getNotSynchronizedUser(
        timestamp: Long,
        userId: Int
    ): List<UserWithGoals>
}