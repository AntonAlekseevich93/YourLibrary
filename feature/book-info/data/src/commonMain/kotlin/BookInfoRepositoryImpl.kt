import database.LocalBookInfoDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteBookInfoDataSource
import main_models.BookVo
import main_models.books.BookTimestampVo
import main_models.rest.books.UserBookRemoteDto
import main_models.rest.books.toRemoteDto
import main_models.rest.books.toVo

class BookInfoRepositoryImpl(
    private val localBookInfoDataSource: LocalBookInfoDataSource,
    private val remoteBookInfoDataSource: RemoteBookInfoDataSource,
    private val authorsRepository: AuthorsRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : BookInfoRepository {

    override suspend fun getLocalBookById(bookLocalId: Long): Flow<BookVo?> =
        localBookInfoDataSource.getLocalBookById(bookLocalId, userId = appConfig.userId)
            .map {
                it.firstOrNull()?.let { book ->
                    book.toVo(
                        remoteImageLink = remoteConfig.getImageUrl(
                            imageName = book.imageName,
                            imageFolderId = book.imageFolderId,
                            bookServerId = book.serverId
                        )
                    )
                }
            }

    override suspend fun updateUserBook(book: BookVo) {
        val userId = appConfig.userId
        val bookVo = localBookInfoDataSource.updateBookAndTime(
            book = book.toLocalDto(appConfig.userId),
            userId = userId
        ).toVo(null)
        val response = remoteBookInfoDataSource.updateUserBook(
            userBook = bookVo.toRemoteDto()
        )?.result

        val bookResponseVo = response?.book?.toVo()

        bookResponseVo?.let {
            localBookInfoDataSource.updateBookWithoutUpdateTime(
                it.toLocalDto(userId),
                userId = userId
            )
            updateBooksTimestamp(it.timestampOfUpdating)
        }
    }

    override suspend fun getBookTimestamp(userId: Long) =
        localBookInfoDataSource.getBookTimestamp(userId = userId).toVo()

    override suspend fun addOrUpdateLocalBooks(
        books: List<UserBookRemoteDto>,
        userId: Long
    ) {
        val booksLocalDto =
            books.mapNotNull { it.toVo()?.toLocalDto(userId) }
        localBookInfoDataSource.addOrUpdateBooks(
            booksLocalDto,
            userId = userId
        )
    }

    override suspend fun updateBookTimestamp(lastTimestamp: BookTimestampVo) {
        localBookInfoDataSource.updateBookTimestamp(lastTimestamp.toEntity())
    }

    override suspend fun getNotSynchronizedBooks(userId: Long) =
        localBookInfoDataSource.getNotSynchronizedBooks(userId).map { it.toVo(null).toRemoteDto() }

    private suspend fun updateBooksTimestamp(timestamp: Long) {
        val lastTimestamp = localBookInfoDataSource.getBookTimestamp(appConfig.userId) //todo
        val finalTimestamp: BookTimestampEntity = lastTimestamp.copy(
            thisDeviceTimestamp = timestamp
        )
        localBookInfoDataSource.updateBookTimestamp(finalTimestamp) //todo
    }

}