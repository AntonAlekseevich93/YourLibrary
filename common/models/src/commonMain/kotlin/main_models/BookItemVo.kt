package main_models

data class BookItemVo(
    val id: Int,
    var shelfId: Int,
    var bookName: String,
    var authorName: String,
    var description: String = "",
    var coverUrl: String = "",
    var coverUrlFromParsing: String = "",
    var numbersOfPage: Int = 0,
    var isbn: String = "",
    var quotes: String = "",
    var readingStatus: ReadingStatus = ReadingStatus.PLANNED
)