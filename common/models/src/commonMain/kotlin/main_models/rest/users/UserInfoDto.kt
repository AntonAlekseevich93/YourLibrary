package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserInfoDto(
    @SerialName("tokenExists") val tokenExist: Boolean?,
    @SerialName("isModerator") val isModerator: Boolean?,
)

class UserInfoVo(
    val tokenExist: Boolean,
    val isModerator: Boolean,
)

fun UserInfoDto.toVo(): UserInfoVo? {
    return UserInfoVo(
        tokenExist = tokenExist ?: return null,
        isModerator = isModerator ?: return null
    )
}