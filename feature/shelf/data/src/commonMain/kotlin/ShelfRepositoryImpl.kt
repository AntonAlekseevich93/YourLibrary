import database.LocalShelfDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteShelfDataSource
import main_models.BookItemVo
import main_models.local_models.toVo

class ShelfRepositoryImpl(
    private val remoteShelfDataSource: RemoteShelfDataSource,
    private val localShelfDataSource: LocalShelfDataSource
) : ShelfRepository {

    override suspend fun getBooksByStatusId(statusId: String): Flow<List<BookItemVo>> =
        localShelfDataSource.getBooksByStatusId(statusId)
            .map { list -> list.map { book -> book.toVo() } }

}