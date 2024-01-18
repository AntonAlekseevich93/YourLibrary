import kotlinx.coroutines.flow.Flow
import main_models.BookItemVo

interface ShelfRepository {
    suspend fun getBooksByStatusId(statusId: String): Flow<List<BookItemVo>>

}