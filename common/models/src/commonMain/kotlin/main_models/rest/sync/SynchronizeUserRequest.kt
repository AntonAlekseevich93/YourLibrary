package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.users.UserRemoteDto

@Serializable
data class SynchronizeUserInfoRequest(
    @SerialName("user_info_this_device_last_timestamp") val userInfoThisDeviceTimestamp: Long,
    @SerialName("user_info_other_devices_last_timestamp") val userInfoOtherDevicesTimestamp: Long,
    @SerialName("user_info_content") val userInfo: UserRemoteDto?,
)

@Serializable
data class SynchronizeUserContentResponse(
    @SerialName("currentDeviceUserLastTimestamp")
    val currentDeviceUserLastTimestamp: Long?,
    @SerialName("otherDeviceUserLastTimestamp")
    val otherDeviceUserLastTimestamp: Long?,
    @SerialName("user_info") val userInfo: UserRemoteDto?,
)