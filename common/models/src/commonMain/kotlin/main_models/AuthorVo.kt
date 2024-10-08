package main_models

import java.util.UUID

data class AuthorVo(
    val serverId: Int?,
    val localId: Long?,
    val id: String,
    val name: String,
    val uppercaseName: String,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val isCreatedByUser: Boolean,
    val firstName: String,
    val middleName: String,
    val lastName: String,
) {
    companion object {
        fun generateId() = UUID.randomUUID().toString() //todo подумать над другой реализацией id
        fun getEmptyAuthor() = AuthorVo( //todo need delete this?
            null,
            null,
            "",
            "",
            "",
            0,
            0,
            isCreatedByUser = false,
            "", "", ""
        )
    }
}