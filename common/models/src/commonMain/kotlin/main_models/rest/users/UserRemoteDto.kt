package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.user.UserVo

@Serializable
data class UserRemoteDto(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("email") val email: String?,
    @SerialName("verified") val isVerified: Boolean?,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long,
    @SerialName("isModerator") val isModerator: Boolean,
    @SerialName("userReadingGoalsInYears") val userReadingGoalsInYears: UserReadingGoalsInYearsDto? = null,
)

fun UserRemoteDto.toVo(): UserVo? {
    return UserVo(
        id = id ?: return null,
        name = name ?: return null,
        email = email ?: return null,
        isVerified = isVerified ?: return null,
        isAuth = true,
        isModerator = isModerator,
        timestampOfUpdating = timestampOfUpdating,
        userReadingGoalsInYears = userReadingGoalsInYears?.toVo()
    )
}

fun UserVo.toDto(): UserRemoteDto {
    return UserRemoteDto(
        id = id,
        name = name,
        email = email,
        isVerified = isVerified,
        isModerator = isModerator,
        timestampOfUpdating = timestampOfUpdating,
        userReadingGoalsInYears = userReadingGoalsInYears?.toDto()
    )
}