package main_models.rest.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNotificationRemoteDto(
    @SerialName("pushToken") val pushToken: String?
)

@Serializable
data class UserNotificationVo(
    val pushToken: String
)

fun UserNotificationRemoteDto.toVo(): UserNotificationVo? {
    return UserNotificationVo(pushToken ?: return null)
}
