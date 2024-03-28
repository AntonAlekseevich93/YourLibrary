package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthRegisterRequest(
    @SerialName("name") val userName: String,
    @SerialName("email") val userEmail: String,
    @SerialName("password") val password: String,
)

@Serializable
class AuthRequest(
    @SerialName("email") val userEmail: String,
    @SerialName("password") val password: String,
)

@Serializable
class AuthResponse(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?,
    @SerialName("token") val token: String?,
    @SerialName("verified") val isVerified: Boolean?,
)