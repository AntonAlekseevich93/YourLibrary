import database.LocalBookCreatorDataSource
import main_models.BookItemResponse

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
) : BookCreatorRepository {
    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    override suspend fun createBook() {
        localBookCreatorDataSource.test()
    }

}