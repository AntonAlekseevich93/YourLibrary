import database.LocalShelfDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteShelfDataSource
import main_models.BookItemVo
import main_models.ShelfVo
import main_models.toLocalDto
import main_models.toVo

class ShelfRepositoryImpl(
    private val remoteShelfDataSource: RemoteShelfDataSource,
    private val localShelfDataSource: LocalShelfDataSource
) : ShelfRepository {
    override suspend fun createDefaultShelvesIfNotExist(defaultShelves: List<ShelfVo>) {
        localShelfDataSource.createDefaultShelvesIfNotExist(defaultShelves.map { it.toLocalDto() })
    }

    override suspend fun getAllShelves(): Flow<ShelfVo> =
        localShelfDataSource.getAllShelves().map { it.toVo(emptyList()) }

    override suspend fun getBooksByShelfId(shelfId: String): Flow<List<BookItemVo>> =
        localShelfDataSource.getBooksByShelfId(shelfId)
            .map { list -> list.map { book -> book.toVo() } }

    override suspend fun createShelf(shelfVo: ShelfVo) {
        localShelfDataSource.createShelf(shelfVo.toLocalDto())
    }
}