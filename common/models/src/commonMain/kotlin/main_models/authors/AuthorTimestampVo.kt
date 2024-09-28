package main_models.authors

class AuthorTimestampVo(
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)