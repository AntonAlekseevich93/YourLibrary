package main_models.authors

class AuthorTimestampVo(
    val userId: Long,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)