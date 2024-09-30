package main_models.rest.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.service_development.UserServiceDevelopmentBookVo

@Serializable
data class UserServiceDevelopmentBookRemoteDto(
    @SerialName("id") val id: Long?,
    @SerialName("userId") val userId: Int?,
    @SerialName("userBookId") val userBookId: String?,
    @SerialName("userAuthorId") val userAuthorId: String?,
    @SerialName("originalAuthorId") val originalAuthorId: String?,
    @SerialName("isApproved") val isApproved: Boolean?,
    @SerialName("isModerated") val isModerated: Boolean?,
    @SerialName("updatedByModeratorId") val updatedByModeratorId: Int?,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long?,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long?,
    @SerialName("updatedByDeviceId") val updatedByDeviceId: String?,
    @SerialName("reasonForRefusal") val reasonForRefusal: String?,
)

fun UserServiceDevelopmentBookRemoteDto.toVo(): UserServiceDevelopmentBookVo? {
    return UserServiceDevelopmentBookVo(
        id = id ?: return null,
        userId = userId ?: return null,
        userBookId = userBookId ?: return null,
        userAuthorId = userAuthorId ?: return null,
        originalAuthorId = originalAuthorId ?: return null,
        isApproved = isApproved ?: return null,
        isModerated = isModerated ?: return null,
        updatedByModeratorId = updatedByModeratorId ?: return null,
        timestampOfCreating = timestampOfCreating ?: return null,
        timestampOfUpdating = timestampOfUpdating ?: return null,
        updatedByDeviceId = updatedByDeviceId ?: return null,
        reasonForRefusal = reasonForRefusal ?: return null,
    )
}