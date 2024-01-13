import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo
import main_models.ShelfVo

interface ShelfRepository {
    suspend fun createDefaultShelvesIfNotExist(defaultShelves: List<ShelfVo>)
    suspend fun getAllShelves(): Flow<ShelfVo>
    suspend fun getBooksByShelfId(shelfId: String): Flow<List<BookItemVo>>
    suspend fun createShelf(shelfVo: ShelfVo)
}