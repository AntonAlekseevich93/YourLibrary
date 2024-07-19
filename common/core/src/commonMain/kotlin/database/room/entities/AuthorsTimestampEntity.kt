package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.authors.AuthorTimestampVo

@Entity
data class AuthorsTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Long,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)

fun AuthorsTimestampEntity.toVo() = AuthorTimestampVo(
    userId = userId,
    otherDevicesTimestamp = otherDevicesTimestamp,
    thisDeviceTimestamp = thisDeviceTimestamp
)