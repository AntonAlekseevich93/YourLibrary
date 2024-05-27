package main_models

import java.util.UUID

//todo we can`t use var values in data classes
data class AuthorVo(
    val serverId: Int?,
    val id: String,
    val name: String,
    val uppercaseName: String,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
    val isCreatedByUser: Boolean,
) {
    companion object {
        fun generateId() = UUID.randomUUID().toString() //todo подумать над другой реализацией id
        fun getEmptyAuthor() = AuthorVo( //todo need delete this?
            null,
            "",
            "",
            "",
            0,
            0,


            isCreatedByUser = false
        )
    }
}