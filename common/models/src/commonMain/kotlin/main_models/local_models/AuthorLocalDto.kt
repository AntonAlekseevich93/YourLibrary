package main_models.local_models

import main_models.AuthorVo

/** need update DatabaseUtils if change values **/
data class AuthorLocalDto(
    val serverId: Int?,
    val id: String?,
    val name: String?,
    val uppercaseName: String?,
    val relatedToAuthorId: String?,
    val isMainAuthor: Int?,
    val timestampOfCreating: Long?,
    val timestampOfUpdating: Long?,
)

fun AuthorVo.toDto() = AuthorLocalDto(
    id = id,
    serverId = serverId,
    name = name,
    uppercaseName = name.uppercase(),
    relatedToAuthorId = relatedToAuthorId,
    isMainAuthor = if (isMainAuthor) 0 else 1,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating
)

fun AuthorLocalDto.toVo(
    relatedAuthors: List<AuthorLocalDto>,
    books: List<BookItemLocalDto>
): AuthorVo? {
    return AuthorVo(
        id = id ?: return null,
        serverId = serverId,
        name = name ?: return null,
        uppercaseName = name.uppercase(),
        relatedToAuthorId = relatedToAuthorId,
        isMainAuthor = isMainAuthor == 0,
        timestampOfCreating = timestampOfCreating ?: return null,
        timestampOfUpdating = timestampOfUpdating ?: return null,
        relatedAuthors = relatedAuthors.mapNotNull {
            it.toVo(
                relatedAuthors = emptyList(),
                books = books
            )
        },
        books = books.map { it.toVo() },
    )
}