import database.LocalMainScreenDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemVo
import main_models.path.PathInfoVo
import main_models.path.toVo
import main_models.local_models.toVo

class MainScreenRepositoryImpl(
    private val localMainScreenDataSource: LocalMainScreenDataSource
) : MainScreenRepository {
    override suspend fun getSelectedPathInfo(): Flow<PathInfoVo?> =
        localMainScreenDataSource.getSelectedPathInfo().map { it?.toVo() }

    override suspend fun getAllBooks(): Flow<List<BookItemVo>> =
        localMainScreenDataSource.getAllBooks().map { list -> list.map { item -> item.toVo() } }

}