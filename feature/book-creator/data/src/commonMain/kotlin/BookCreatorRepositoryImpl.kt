import database.LocalBookCreatorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.ShelfVo
import main_models.toLocalDto
import main_models.toVo

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
) : BookCreatorRepository {
    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    override suspend fun createBook(bookItemVo: BookItemVo) {
        localBookCreatorDataSource.createBook(bookItemVo.toLocalDto())
    }

    override suspend fun getShelvesWithoutBooks(): Flow<List<ShelfVo>> =
        localBookCreatorDataSource.getShelves()
            .map { list -> list.map { shelfDto -> shelfDto.toVo(emptyList()) } }

}