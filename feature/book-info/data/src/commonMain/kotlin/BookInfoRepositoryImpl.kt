import database.LocalBookInfoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemVo
import main_models.ReadingStatus
import main_models.path.PathInfoVo
import main_models.path.toVo
import main_models.toLocalDto
import main_models.toVo

class BookInfoRepositoryImpl(
    private val localBookInfoDataSource: LocalBookInfoDataSource
) : BookInfoRepository {
    override suspend fun getBookById(bookId: String): Flow<BookItemVo> =
        localBookInfoDataSource.getBookById(bookId).map { it.toVo() }

    override suspend fun getSelectedPathInfo(): Flow<PathInfoVo?> =
        localBookInfoDataSource.getSelectedPathInfo().map { it?.toVo() }

    override suspend fun updateBook(bookItem: BookItemVo) {
        localBookInfoDataSource.updateBook(bookItem.toLocalDto())
    }

    override suspend fun changeBookStatusId(readingStatus: ReadingStatus, bookId: String) {
        localBookInfoDataSource.changeBookStatusId(
            statusId = readingStatus.id,
            readingStatus = readingStatus.nameValue,
            bookId = bookId,
        )
    }

}