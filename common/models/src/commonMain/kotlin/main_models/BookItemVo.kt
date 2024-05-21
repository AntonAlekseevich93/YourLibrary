package main_models

import java.util.UUID

//todo changed by BookVo. Will need delete
data class BookItemVo(
    val id: String,
    val originalAuthorId: String,
    val modifiedAuthorId: String?,
    val statusId: String,
    val shelfId: String? = null,
    val bookName: String,
    val originalAuthorName: String,
    val modifiedAuthorName: String?,
    val description: String = "",
    val coverUrl: String = "",
    val coverUrlFromParsing: String = "",
    val numbersOfPages: Int = 0,
    val isbn: String = "",
    val quotes: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.PLANNED,
    val startDateInString: String = "",
    val endDateInString: String = "",
    val startDateInMillis: Long = -1,
    val endDateInMillis: Long = -1,
    val timestampOfCreating: Long,
    val timestampOfUpdating: Long,
) {
    companion object {
        fun generateId() = UUID.randomUUID().toString() //todo подумать над другой реализацией id
    }
}