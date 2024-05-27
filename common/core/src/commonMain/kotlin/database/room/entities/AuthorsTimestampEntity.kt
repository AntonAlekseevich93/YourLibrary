package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthorsTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Long,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)