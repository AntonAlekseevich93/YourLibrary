package main_models.rest.authors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.AuthorVo

@Serializable
data class AuthorsResponse(
    @SerialName("authors") val authors: List<AuthorResponse>
)

@Serializable
data class AuthorResponse(
    @SerialName("id") val serverId: Int? = null,
    @SerialName("authorId") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("uppercaseName") val uppercaseName: String? = null,
    @SerialName("isApproved") val isApproved: Boolean? = null,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long? = null,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long? = null,
    @SerialName("isCreatedByUser") val isCreatedByUser: Boolean? = null,
    @SerialName("firstName") val firstName: String?,
    @SerialName("middleName") val middleName: String?,
    @SerialName("lastName") val lastName: String?,
)

fun AuthorResponse.toAuthorVo(): AuthorVo? {
    return AuthorVo(
        id = id ?: return null,
        localId = null,
        serverId = serverId ?: return null,
        name = name ?: return null,
        uppercaseName = name.uppercase(),
        timestampOfCreating = timestampOfCreating ?: return null,
        timestampOfUpdating = timestampOfUpdating ?: return null,
        isCreatedByUser = isCreatedByUser ?: return null,
        firstName = firstName ?: return null,
        middleName = middleName ?: return null,
        lastName = lastName ?: return null,
    )
}