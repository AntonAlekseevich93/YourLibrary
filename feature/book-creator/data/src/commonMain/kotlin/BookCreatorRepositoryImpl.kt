import database.LocalBookCreatorDataSource
import database.room.entities.toBookEntity
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.BookVo
import main_models.local_models.toLocalDto

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
) : BookCreatorRepository {

    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    @Deprecated("replaced by room db")
    override suspend fun createBook(bookItemVo: BookItemVo) {
        localBookCreatorDataSource.createBook(bookItemVo.toLocalDto())
    }

    override suspend fun createBook(book: BookVo) {
        localBookCreatorDataSource.createBook(book.toBookEntity())
    }
}