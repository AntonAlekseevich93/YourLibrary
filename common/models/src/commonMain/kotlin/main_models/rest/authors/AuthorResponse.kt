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
)

fun AuthorResponse.toAuthorVo(): AuthorVo? {
    return AuthorVo(
        id = id ?: return null,
        serverId = serverId ?: return null,
        name = name ?: return null,
        uppercaseName = name.uppercase(),
        relatedToAuthorId = null,
        isMainAuthor = true,
        timestampOfCreating = timestampOfCreating ?: return null,
        timestampOfUpdating = timestampOfUpdating ?: return null,
        books = emptyList()
    )
}