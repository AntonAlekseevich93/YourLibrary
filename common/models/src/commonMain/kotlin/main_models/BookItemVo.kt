package main_models

import java.util.UUID

//todo we can`t use var values in data classes
data class BookItemVo(
    val id: String,
    val originalAuthorId: String,
    val modifiedAuthorId: String?,
    var statusId: String,
    var shelfId: String? = null,
    var bookName: String,
    var originalAuthorName: String,
    var modifiedAuthorName: String?,
    var description: String = "",
    var coverUrl: String = "",
    var coverUrlFromParsing: String = "",
    var numbersOfPages: Int = 0,
    var isbn: String = "",
    var quotes: String = "",
    var readingStatus: ReadingStatus = ReadingStatus.PLANNED,
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