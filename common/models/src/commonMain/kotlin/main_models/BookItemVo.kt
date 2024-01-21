package main_models

import java.util.UUID

data class BookItemVo(
    val id: String,
    var statusId: String,
    var shelfId: String? = null,
    var bookName: String,
    var authorName: String,
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