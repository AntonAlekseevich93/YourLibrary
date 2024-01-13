package main_models

import java.util.UUID

data class BookItemVo(
    val id: String,
    var shelfId: String,
    var bookName: String,
    var authorName: String,
    var description: String = "",
    var coverUrl: String = "",
    var coverUrlFromParsing: String = "",
    var numbersOfPages: Int = 0,
    var isbn: String = "",
    var quotes: String = "",
    var readingStatus: ReadingStatus = ReadingStatus.PLANNED,
) {
    companion object {
        fun generateId() = UUID.randomUUID().toString() //todo подумать над другой реализацией id
    }
}