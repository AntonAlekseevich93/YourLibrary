import database.LocalBooksListInfoDataSource
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteBooksListInfoDataSource
import main_models.BookVo

class BooksListInfoRepositoryImpl(
    private val localBooksListInfoDataSource: LocalBooksListInfoDataSource,
    private val remoteBooksListInfoDataSource: RemoteBooksListInfoDataSource,
    private val authorsRepository: AuthorsRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : BooksListInfoRepository {

    override suspend fun getLocalBookByLocalId(bookLocalId: Long): Flow<BookVo?> =
        localBooksListInfoDataSource.getLocalBookByLocalId(bookLocalId, userId = appConfig.userId)
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

    override suspend fun getLocalBookById(bookId: String): Flow<BookVo?> =
        localBooksListInfoDataSource.getLocalBookById(bookId, userId = appConfig.userId)
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
}