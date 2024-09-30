package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.rest.books.UserServiceDevelopmentBookRemoteDto
import main_models.service_development.UserServiceDevelopmentBookVo

@Entity
data class UserServiceDevelopmentBookEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("localId") val localId: Long? = null,
    @SerialName("id") val id: Long,
    @SerialName("userId") val userId: Int,
    @SerialName("userBookId") val userBookId: String,
    @SerialName("userAuthorId") val userAuthorId: String,
    @SerialName("originalAuthorId") val originalAuthorId: String,
    @SerialName("isApproved") val isApproved: Boolean,
    @SerialName("isModerated") val isModerated: Boolean,
    @SerialName("updatedByModeratorId") val updatedByModeratorId: Int,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long,
    @SerialName("updatedByDeviceId") val updatedByDeviceId: String,
    @SerialName("reasonForRefusal") val reasonForRefusal: String,
)

fun UserServiceDevelopmentBookVo.toEntity(): UserServiceDevelopmentBookEntity {
    return UserServiceDevelopmentBookEntity(
        id = id,
        userId = userId,
        userBookId = userBookId,
        userAuthorId = userAuthorId,
        originalAuthorId = originalAuthorId,
        isApproved = isApproved,
        isModerated = isModerated,
        updatedByModeratorId = updatedByModeratorId,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        updatedByDeviceId = updatedByDeviceId,
        reasonForRefusal = reasonForRefusal,
    )
}

fun UserServiceDevelopmentBookEntity.toRemoteDto(): UserServiceDevelopmentBookRemoteDto? {
    return UserServiceDevelopmentBookRemoteDto(
        id = id,
        userId = userId,
        userBookId = userBookId,
        userAuthorId = userAuthorId,
        originalAuthorId = originalAuthorId,
        isApproved = isApproved,
        isModerated = isModerated,
        updatedByModeratorId = updatedByModeratorId,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        updatedByDeviceId = updatedByDeviceId,
        reasonForRefusal = reasonForRefusal,
    )
}

fun UserServiceDevelopmentBookEntity.toVo(): UserServiceDevelopmentBookVo? {
    return UserServiceDevelopmentBookVo(
        id = id,
        userId = userId,
        userBookId = userBookId,
        userAuthorId = userAuthorId,
        originalAuthorId = originalAuthorId,
        isApproved = isApproved,
        isModerated = isModerated,
        updatedByModeratorId = updatedByModeratorId,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        updatedByDeviceId = updatedByDeviceId,
        reasonForRefusal = reasonForRefusal,
    )
}