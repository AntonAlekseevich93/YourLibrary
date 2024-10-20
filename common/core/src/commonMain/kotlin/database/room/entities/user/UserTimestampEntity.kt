package database.room.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.user.UserTimestampVo

@Entity
data class UserTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)

fun UserTimestampEntity.toVo() = UserTimestampVo(
    userId = userId,
    thisDeviceTimestamp = thisDeviceTimestamp,
    otherDevicesTimestamp = otherDevicesTimestamp,
)

fun UserTimestampVo.toEntity() = UserTimestampEntity(
    userId = userId,
    thisDeviceTimestamp = thisDeviceTimestamp,
    otherDevicesTimestamp = otherDevicesTimestamp,
)