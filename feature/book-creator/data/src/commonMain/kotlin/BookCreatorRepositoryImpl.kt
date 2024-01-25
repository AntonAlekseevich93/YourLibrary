import database.LocalBookCreatorDataSource
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.local_models.toLocalDto

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
) : BookCreatorRepository {

    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    override suspend fun createBook(bookItemVo: BookItemVo) {
        localBookCreatorDataSource.createBook(bookItemVo.toLocalDto())
    }

}