package main_models.books

data class BookTimestampVo(
    val userId: Long,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)