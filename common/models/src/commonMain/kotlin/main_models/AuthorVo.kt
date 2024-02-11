package main_models

import java.util.UUID

//todo we can`t use var values in data classes
data class AuthorVo(
    val id: String,
    var name: String,
    var relatedToAuthorId: String?,
    var isMainAuthor: Boolean,
    val timestampOfCreating: Long,
    var timestampOfUpdating: Long,
    var relatedAuthors: List<AuthorVo> = emptyList(),
    var books: List<BookItemVo>,
) {
    companion object {
        fun generateId() = UUID.randomUUID().toString() //todo подумать над другой реализацией id
        fun getEmptyAuthor() = AuthorVo(
            "",
            "",
            null,
            true,
            0,
            0,
            books = emptyList()
        )
    }
}