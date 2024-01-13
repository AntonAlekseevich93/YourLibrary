package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemLocalDto

class LocalBookInfoDataSource(
    private val db: SqlDelightDataSource
) {

    suspend fun getBookById(bookId: String): Flow<BookItemLocalDto> =
        db.appQuery.getBookById(bookId).asFlow().mapToOne(Dispatchers.IO).map { item -> item.map() }
}