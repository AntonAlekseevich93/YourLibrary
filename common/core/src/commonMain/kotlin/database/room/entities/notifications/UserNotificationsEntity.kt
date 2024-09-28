package database.room.entities.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserNotificationsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val userId: Int,
    val deviceId: String,
    val pushToken: String,
)