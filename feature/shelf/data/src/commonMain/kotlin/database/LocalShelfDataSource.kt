package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.local_models.BookItemLocalDto

class LocalShelfDataSource(
    private val db: SqlDelightDataSource
) {

    suspend fun getBooksByStatusId(statusId: String): Flow<List<BookItemLocalDto>> =
        db.appQuery.getAllBooksByStatusId(statusId).asFlow().mapToList(Dispatchers.IO)
            .map { list -> list.map { booksTable -> booksTable.map() } }

}