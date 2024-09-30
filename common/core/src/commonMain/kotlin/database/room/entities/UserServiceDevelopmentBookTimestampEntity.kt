package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.service_development.ServiceDevelopmentBooksTimestampVo

@Entity
data class UserServiceDevelopmentBookTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)

fun UserServiceDevelopmentBookTimestampEntity.toVo(): ServiceDevelopmentBooksTimestampVo =
    ServiceDevelopmentBooksTimestampVo(
        userId = userId,
        otherDevicesTimestamp = otherDevicesTimestamp,
        thisDeviceTimestamp = thisDeviceTimestamp,
    )

fun ServiceDevelopmentBooksTimestampVo.toEntity(): UserServiceDevelopmentBookTimestampEntity =
    UserServiceDevelopmentBookTimestampEntity(
        userId = userId,
        otherDevicesTimestamp = otherDevicesTimestamp,
        thisDeviceTimestamp = thisDeviceTimestamp,
    )