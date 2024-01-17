import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo
import main_models.path.PathInfoVo

interface BookInfoRepository {
    suspend fun getSelectedPathInfo(): Flow<PathInfoVo?>
    suspend fun getBookById(bookId: String): Flow<BookItemVo>
}