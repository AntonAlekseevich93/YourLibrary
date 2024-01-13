package database

import DatabaseUtils.Companion.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import main_models.BookItemLocalDto
import main_models.ShelfLocalDto

class LocalShelfDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun createDefaultShelvesIfNotExist(defaultShelves: List<ShelfLocalDto>) {
        if (db.appQuery.getAllShelves().executeAsList().isEmpty()) {
            defaultShelves.forEach {
                createShelf(it)
            }
        }
    }

    suspend fun getAllShelves(): Flow<ShelfLocalDto> = channelFlow {
        db.appQuery.getAllShelves().asFlow().collect { shelves ->
            shelves.executeAsList().forEach { shelf ->
                trySend(shelf.map())
            }
        }
    }

    suspend fun getBooksByShelfId(shelfId: String): Flow<List<BookItemLocalDto>> =
        db.appQuery.getAllBooksByShelfId(shelfId).asFlow().mapToList(Dispatchers.IO)
            .map { list -> list.map { booksTable -> booksTable.map() } }

    suspend fun createShelf(shelfLocalDto: ShelfLocalDto) {
        shelfLocalDto.apply {
            db.appQuery.addShelf(
                id = id,
                name = name,
                description = description
            )
        }
    }
}