package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import main_models.books.BookTimestampVo

@Entity
data class BookTimestampEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)

fun BookTimestampEntity.toVo() = BookTimestampVo(
    userId = userId,
    otherDevicesTimestamp = otherDevicesTimestamp,
    thisDeviceTimestamp = thisDeviceTimestamp
)

fun BookTimestampVo.toEntity() = BookTimestampEntity(
    userId = userId,
    otherDevicesTimestamp = otherDevicesTimestamp,
    thisDeviceTimestamp = thisDeviceTimestamp
)