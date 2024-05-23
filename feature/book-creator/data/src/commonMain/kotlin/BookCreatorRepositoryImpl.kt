import database.LocalBookCreatorDataSource
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteBookCreatorDataSource
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.BookVo
import main_models.local_models.toLocalDto
import main_models.rest.books.toRemoteDto

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val remoteBookCreatorDataSource: RemoteBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
) : BookCreatorRepository {

    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    @Deprecated("replaced by room db")
    override suspend fun createBook(bookItemVo: BookItemVo) {
        localBookCreatorDataSource.createBook(bookItemVo.toLocalDto())
    }

    override suspend fun createBook(book: BookVo) {
        val bookVo = localBookCreatorDataSource.createBook(book.toLocalDto()).toVo()
        val response = remoteBookCreatorDataSource.addNewUserBook(
            userBook = bookVo.toRemoteDto()
        )?.result?.book
    }
}