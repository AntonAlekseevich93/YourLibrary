package database

import main_models.local_models.BookItemLocalDto

class LocalBookCreatorDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun createBook(bookItem: BookItemLocalDto) {
        bookItem.apply {
            db.appQuery.addBook(
                id = id,
                originalAuthorId = originalAuthorId,
                modifiedAuthorId = modifiedAuthorId,
                statusId = statusId,
                shelfId = shelfId,
                bookName = bookName,
                originalAuthorName = originalAuthorName,
                modifiedAuthorName = modifiedAuthorName,
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