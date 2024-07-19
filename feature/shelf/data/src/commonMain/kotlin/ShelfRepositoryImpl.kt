import database.LocalShelfDataSource
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ktor.RemoteShelfDataSource
import main_models.BookVo

class ShelfRepositoryImpl(
    private val remoteShelfDataSource: RemoteShelfDataSource,
    private val localShelfDataSource: LocalShelfDataSource,
    private val remoteConfig: RemoteConfig,
    private val bookInfoRepository: BookInfoRepository
) : ShelfRepository {

    override suspend fun getAllBooksByReadingStatus(readingStatus: String): Flow<List<BookVo>> =
        localShelfDataSource.getAllBooks(readingStatus)
            .map { list ->
                list.map { book ->
                    book.toVo(
                        remoteImageLink = remoteConfig.getImageUrl(
                            imageName = book.imageName,
                            imageFolderId = book.imageFolderId,
                            bookServerId = book.serverId
                        )
                    )
                }
            }

    override suspend fun synchronizeBooksWithAuthors() =
        bookInfoRepository.getAllRemoteBooksWithAuthorsByTimestamps()


}