import database.LocalBookCreatorDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteBookCreatorDataSource
import main_models.BookVo
import main_models.ReadingStatus
import main_models.ReadingStatusUtils
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toRemoteDto
import main_models.rest.books.toVo

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource,
    private val remoteBookCreatorDataSource: RemoteBookCreatorDataSource,
    private val appConfig: AppConfig,
    private val authorsRepository: AuthorsRepository,
) : BookCreatorRepository {

    override suspend fun createBook(book: BookVo) {
        val bookVo =
            localBookCreatorDataSource.createBook(book.toLocalDto(userId = appConfig.userId))
                .toVo(null)
        val response = remoteBookCreatorDataSource.addNewUserBook(
            userBook = bookVo.toRemoteDto()
        )?.result

        val bookResponseVo = response?.book?.toVo()
        val authorResponseVo = response?.author?.toAuthorVo()

        bookResponseVo?.let {
            localBookCreatorDataSource.updateBook(it.toLocalDto(appConfig.userId))
            updateBooksTimestamp(it.timestampOfUpdating)
        }

        authorResponseVo?.let {
            authorsRepository.updateAuthorInLocalDb(it)
            authorsRepository.updateThisDeviceAuthorsTimestamp(
                thisDeviceTimestamp = it.timestampOfUpdating,
                otherDeviceTimestamp = null
            )
        }
    }

    override suspend fun getBookStatusByBookId(bookId: String): ReadingStatus? {
        localBookCreatorDataSource.getBookStatusByBookId(bookId)?.let {
            return ReadingStatusUtils.textToReadingStatus(it)
        }
        return null
    }

    private suspend fun updateBooksTimestamp(timestamp: Long) {
        val lastTimestamp = localBookCreatorDataSource.getBookTimestamp(appConfig.userId)
        val finalTimestamp: BookTimestampEntity = lastTimestamp.copy(
            thisDeviceTimestamp = timestamp
        )
        localBookCreatorDataSource.updateBookTimestamp(finalTimestamp)
    }
}