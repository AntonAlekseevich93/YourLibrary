package main_models

/** need update DatabaseUtils if change values **/
data class BookItemLocalDto(
    val id: String,
    var statusId: String,
    var shelfId: String?,
    var bookName: String,
    var authorName: String,
    var description: String? = "",
    var coverUrl: String? = "",
    var coverUrlFromParsing: String? = "",
    var numbersOfPages: Int? = 0,
    var isbn: String? = "",
    var quotes: String? = "",
    var readingStatus: String?,
    val startDateInString: String? = "",
    val endDateInString: String? = "",
    val startDateInMillis: Long? = -1,
    val endDateInMillis: Long? = -1,
    val timestampOfCreating: Long? = -1,
    val timestampOfUpdating: Long? = -1,
)

fun BookItemVo.toLocalDto() = BookItemLocalDto(
    id = id,
    statusId = statusId,
    shelfId = shelfId,
    bookName = bookName,
    authorName = authorName,
    description = description,
    coverUrl = coverUrl,
    coverUrlFromParsing = coverUrlFromParsing,
    numbersOfPages = numbersOfPages,
    isbn = isbn,
    quotes = quotes,
    readingStatus = readingStatus.nameValue,
    startDateInString = startDateInString,
    endDateInString = endDateInString,
    startDateInMillis = startDateInMillis,
    endDateInMillis = endDateInMillis,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
)

fun BookItemLocalDto.toVo() = BookItemVo(
    id = id,
    statusId = statusId,
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
    },
    startDateInString = startDateInString ?: "",
    endDateInString = endDateInString ?: "",
    startDateInMillis = startDateInMillis ?: -1,
    endDateInMillis = endDateInMillis ?: -1,
    timestampOfCreating = timestampOfCreating ?: -1,
    timestampOfUpdating = timestampOfUpdating ?: -1,
)