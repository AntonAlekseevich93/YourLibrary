package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserStatus(
    @SerialName("tokenExists") val tokenExist: Boolean?
)