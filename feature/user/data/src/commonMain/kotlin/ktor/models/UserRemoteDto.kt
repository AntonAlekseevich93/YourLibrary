package ktor.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.user.UserVo

@Serializable
data class UserRemoteDto(
    @SerialName("id") val id: Long?,
    @SerialName("name") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("verified") val isVerified: Boolean?
)

fun UserRemoteDto.toVo(): UserVo? {
    return UserVo(
        id = id ?: return null,
        name = name ?: return null,
        email = email ?: return null,
        isVerified = isVerified ?: return null,
    )
}