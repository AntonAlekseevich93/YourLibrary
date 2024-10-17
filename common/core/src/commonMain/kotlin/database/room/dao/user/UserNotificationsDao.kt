package database.room.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import database.room.entities.notifications.UserNotificationsEntity

@Dao
interface UserNotificationsDao {
    @Query("UPDATE UserNotificationsEntity set pushToken = :pushToken WHERE userId =:userId and deviceId = :deviceId")
    suspend fun updateNotification(pushToken: String, deviceId: String, userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(data: UserNotificationsEntity)

    @Query("SELECT * FROM UserNotificationsEntity WHERE userId = :userId and deviceId = :deviceId")
    suspend fun getNotifications(userId: Int, deviceId: String): List<UserNotificationsEntity>

    @Query("DELETE FROM UserNotificationsEntity")
    suspend fun deleteAllData()
}