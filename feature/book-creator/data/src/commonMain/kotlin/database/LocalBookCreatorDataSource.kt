package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemLocalDto
import main_models.ShelfLocalDto

class LocalBookCreatorDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun createBook(bookItem: BookItemLocalDto) {
        bookItem.apply {
            db.appQuery.addBook(
                id = id,
                shelfId = shelfId,
                bookName = bookName,
                authorName = authorName,
                description = description,
                coverUrl = coverUrl,
                coverUrlFromParsing = coverUrlFromParsing,
                numbersOfPages = numbersOfPages?.toLong(),
                isbn = isbn,
                quotes = quotes,
                readingStatus = readingStatus
            )
        }
    }

    fun getShelves(): Flow<List<ShelfLocalDto>> =
        db.appQuery.getAllShelves().asFlow()
            .map { it.executeAsList().map { shelves -> shelves.map() } }

}