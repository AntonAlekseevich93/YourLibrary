import database.LocalBookCreatorDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteBookCreatorDataSource
import main_models.AuthorVo
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
    private val remoteConfig: RemoteConfig,
) : BookCreatorRepository {

    override suspend fun createBook(book: BookVo, author: AuthorVo) {
        val userId = appConfig.userId
        val bookVo = localBookCreatorDataSource.createBook(
            book = book.toLocalDto(),
            userId = userId
        ).toVo(null)
        authorsRepository.createAuthorIfNotExist(author)
        val response = remoteBookCreatorDataSource.addNewUserBook(
            userBook = bookVo.toRemoteDto(),
        )?.result

        val bookResponseVo = response?.book?.toVo()
        val authorResponseVo: AuthorVo? = response?.author?.toAuthorVo()

        bookResponseVo?.let {
            localBookCreatorDataSource.updateBookWithoutUpdateTime(
                it.toLocalDto(),
                userId = userId
            )
            updateBooksTimestamp(it.timestampOfUpdating)
        }

        authorResponseVo?.let {
            authorsRepository.updateAuthorInLocalDb(it)
            authorsRepository.updateAuthorsTimestamp(
                thisDeviceTimestamp = it.timestampOfUpdating,
                otherDeviceTimestamp = null
            )
        }
    }

    override suspend fun getBookStatusByBookId(bookId: String): ReadingStatus? {
        localBookCreatorDataSource.getBookStatusByBookId(bookId, userId = appConfig.userId)?.let {
            return ReadingStatusUtils.textToReadingStatus(it)
        }
        return null
    }

    override suspend fun getLocalBookById(bookId: String): BookVo? {
        val book =
            localBookCreatorDataSource.getLocalBookById(bookId = bookId, userId = appConfig.userId)
        return book?.toVo(
            remoteImageLink = remoteConfig.getImageUrl(
                imageName = book.imageName,
                imageFolderId = book.imageFolderId,
                bookServerId = book.serverId
            )
        )
    }

    override suspend fun getLocalAuthorById(authorId: String): AuthorVo? =
        authorsRepository.getLocalAuthorById(authorId)

    private suspend fun updateBooksTimestamp(timestamp: Long) {
        val lastTimestamp = localBookCreatorDataSource.getBookTimestamp(appConfig.userId)
        val finalTimestamp: BookTimestampEntity = lastTimestamp.copy(
            thisDeviceTimestamp = timestamp
        )
        localBookCreatorDataSource.updateBookTimestamp(finalTimestamp)
    }
}