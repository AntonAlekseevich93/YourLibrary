package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemLocalDto
import main_models.path.PathInfoDto

class LocalBookInfoDataSource(
    private val db: SqlDelightDataSource
) {

    suspend fun getBookById(bookId: String): Flow<BookItemLocalDto> =
        db.appQuery.getBookById(bookId).asFlow().mapToOne(Dispatchers.IO).map { item -> item.map() }

    suspend fun getSelectedPathInfo(): Flow<PathInfoDto?> = db.getSelectedPathInfo()

    suspend fun updateBook(bookItem: BookItemLocalDto) {
        bookItem.apply {
            db.appQuery.updateBook(
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
            )
        }
    }

    suspend fun changeBookStatusId(statusId: String, readingStatus: String, bookId: String) {
        db.appQuery.updateBookStatusId(statusId, readingStatus, bookId)
    }

}