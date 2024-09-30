package main_models.service_development

import main_models.rest.books.UserServiceDevelopmentBookRemoteDto

data class UserServiceDevelopmentBookVo(
    val id: Long,
    val userId: Int,
    val userBookId: String,
    val userAuthorId: String,
    val originalAuthorId: String,
    val isApproved: Boolean,
    val isModerated: Boolean,
    val updatedByModeratorId: Int,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val updatedByDeviceId: String,
    val reasonForRefusal: String,
)

fun UserServiceDevelopmentBookVo.toRemoteDto(): UserServiceDevelopmentBookRemoteDto =
    UserServiceDevelopmentBookRemoteDto(
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