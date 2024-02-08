package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.local_models.BookItemLocalDto

class LocalApplicationDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun getAllBooks(): Flow<List<BookItemLocalDto>> =
        db.appQuery.getAllBooks().asFlow().mapToList(Dispatchers.IO)
            .map { list -> list.map { item -> item.map() } }

}