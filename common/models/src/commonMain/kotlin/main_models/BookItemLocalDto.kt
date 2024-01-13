package main_models

data class BookItemLocalDto(
    val id: String,
    var shelfId: String,
    var bookName: String,
    var authorName: String,
    var description: String? = "",
    var coverUrl: String? = "",
    var coverUrlFromParsing: String? = "",
    var numbersOfPages: Int? = 0,
    var isbn: String? = "",
    var quotes: String? = "",
    var readingStatus: String?,
)

fun BookItemVo.toLocalDto() = BookItemLocalDto(
    id = id,
    shelfId = shelfId,
    bookName = bookName,
    authorName = authorName,
    description = description,
    coverUrl = coverUrl,
    coverUrlFromParsing = coverUrlFromParsing,
    numbersOfPages = numbersOfPages,
    isbn = isbn,
    quotes = quotes,
    readingStatus = readingStatus.nameValue
)

fun BookItemLocalDto.toVo() = BookItemVo(
    id = id,
    shelfId = shelfId,
    bookName = bookName,
    authorName = authorName,
    description = description ?: "",
    coverUrl = coverUrl ?: "",
    coverUrlFromParsing = coverUrlFromParsing ?: "",
    numbersOfPages = numbersOfPages ?: 0,
    isbn = isbn ?: "",
    quotes = quotes ?: "",
    readingStatus = when (readingStatus) {
        ReadingStatus.PLANNED.nameValue -> ReadingStatus.PLANNED
        ReadingStatus.READING.nameValue -> ReadingStatus.READING
        ReadingStatus.DONE.nameValue -> ReadingStatus.DONE
        ReadingStatus.DEFERRED.nameValue -> ReadingStatus.DEFERRED
        else -> {
            ReadingStatus.PLANNED
        }
    }
)