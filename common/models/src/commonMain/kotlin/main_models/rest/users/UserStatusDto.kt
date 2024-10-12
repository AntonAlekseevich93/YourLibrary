package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserStatusDto(
    @SerialName("tokenExists") val tokenExist: Boolean?,
    @SerialName("isModerator") val isModerator: Boolean?,
)

class UserStatusVo(
    val tokenExist: Boolean,
    val isModerator: Boolean,
)

fun UserStatusDto.toVo(): UserStatusVo? {
    return UserStatusVo(
        tokenExist = tokenExist ?: return null,
        isModerator = isModerator ?: return null
    )
}