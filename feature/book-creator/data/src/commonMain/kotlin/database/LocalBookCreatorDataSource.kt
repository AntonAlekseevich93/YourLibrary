package database

import main_models.BookItemLocalDto

class LocalBookCreatorDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun createBook(bookItem: BookItemLocalDto) {
        bookItem.apply {
            db.appQuery.addBook(
                id = id,
                statusId = statusId,
                shelfId = shelfId,
                bookName = bookName,
                authorName = authorName,
                description = description,
                coverUrl = coverUrl,
                coverUrlFromParsing = coverUrlFromParsing,
                numbersOfPages = numbersOfPages?.toLong(),
                isbn = isbn,
                quotes = quotes,
                readingStatus = readingStatus,
                startDateInString = startDateInString,
                endDateInString = endDateInString,
                startDateInMillis = startDateInMillis,
                endDateInMillis = endDateInMillis,
                timestampOfCreating = timestampOfCreating,
                timestampOfUpdating = timestampOfUpdating,
            )
        }
    }
}