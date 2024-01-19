import database.LocalMainScreenDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.path.PathInfoVo
import main_models.path.toVo

class MainScreenRepositoryImpl(
    private val localMainScreenDataSource: LocalMainScreenDataSource
) : MainScreenRepository {
    override suspend fun getSelectedPathInfo(): Flow<PathInfoVo?> =
        localMainScreenDataSource.getSelectedPathInfo().map { it?.toVo() }

}