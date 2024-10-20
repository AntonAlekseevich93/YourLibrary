package main_models.user

data class UserTimestampVo(
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)
