package main_models.service_development

data class ServiceDevelopmentBooksTimestampVo(
    val userId: Int,
    val otherDevicesTimestamp: Long,
    val thisDeviceTimestamp: Long,
)
