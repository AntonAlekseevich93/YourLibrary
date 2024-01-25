import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo
import main_models.path.PathInfoVo

interface MainScreenRepository {
    suspend fun getSelectedPathInfo(): Flow<PathInfoVo?>
    suspend fun getAllBooks(): Flow<List<BookItemVo>>
}