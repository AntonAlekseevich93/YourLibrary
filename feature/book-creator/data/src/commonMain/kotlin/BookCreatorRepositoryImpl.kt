import database.LocalBookCreatorDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteBookCreatorDataSource
import main_models.BookItemResponse
import main_models.BookItemVo
import main_models.BookVo
import main_models.local_models.toLocalDto
import main_models.rest.books.toRemoteDto
import main_models.rest.books.toVo

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val remoteBookCreatorDataSource: RemoteBookCreatorDataSource,
    private val urlParserInteractor: UrlParserInteractor,
    private val appConfig: AppConfig,
) : BookCreatorRepository {

    override suspend fun parseBookUrl(url: String): BookItemResponse =
        urlParserInteractor.parseBookUrl(url)

    @Deprecated("replaced by room db")
    override suspend fun createBook(bookItemVo: BookItemVo) {
        localBookCreatorDataSource.createBook(bookItemVo.toLocalDto())
    }

    override suspend fun createBook(book: BookVo) {
        val bookVo =
            localBookCreatorDataSource.createBook(book.toLocalDto(userId = appConfig.userId)).toVo()
        val response = remoteBookCreatorDataSource.addNewUserBook(
            userBook = bookVo.toRemoteDto()
        )?.result?.book?.toVo()

        response?.let {
            localBookCreatorDataSource.updateBook(it.toLocalDto(appConfig.userId))
            updateYourBookTimestamp(it.timestampOfUpdating)
        }
    }

    private suspend fun updateYourBookTimestamp(timestamp: Long) {
        val lastTimestamp = localBookCreatorDataSource.getBookTimestamp(appConfig.userId)
        val finalTimestamp: BookTimestampEntity = lastTimestamp?.copy(
            thisDeviceTimestamp = timestamp
        ) ?: BookTimestampEntity(
            userId = appConfig.userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = timestamp
        )
        localBookCreatorDataSource.updateBookTimestamp(finalTimestamp)
    }
}