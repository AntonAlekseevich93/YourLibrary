import database.LocalBookInfoDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toLocalDto
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ktor.RemoteBookInfoDataSource
import main_models.BookVo
import main_models.rest.SynchronizeBooksWithAuthorsRequest
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toRemoteDto
import main_models.rest.books.toVo

class BookInfoRepositoryImpl(
    private val localBookInfoDataSource: LocalBookInfoDataSource,
    private val remoteBookInfoDataSource: RemoteBookInfoDataSource,
    private val authorsRepository: AuthorsRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : BookInfoRepository {
    private val mutex = Mutex()

    override suspend fun synchronizeBooksWithAuthors(): Boolean {
        return if (appConfig.isAuth) {
            synchronize()
        } else false
    }

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

    private suspend fun synchronize(): Boolean {
        return mutex.withLock {
            val userId = appConfig.userId
            val response = remoteBookInfoDataSource.getAllBooksAndAuthorsByTimestamp(
                body = getSynchronizeBody(userId)
            )
            var success = false
            response?.result?.missingBooksAndAuthorsFromServer?.let { result ->
                result.booksOtherDevices?.let { otherDevicesBooks ->
                    val booksTimestamp = localBookInfoDataSource.getBookTimestamp(userId = userId)
                    val lastTimestamp = otherDevicesBooks.takeIf { it.isNotEmpty() }
                        ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
                    if (otherDevicesBooks.isNotEmpty() && lastTimestamp != null) {
                        val booksLocalDto =
                            otherDevicesBooks.mapNotNull { it.toVo()?.toLocalDto(userId) }
                        localBookInfoDataSource.addOrUpdateBooks(booksLocalDto, userId = userId)
                        localBookInfoDataSource.updateBookTimestamp(
                            booksTimestamp.copy(
                                otherDevicesTimestamp = lastTimestamp
                            )
                        )
                    }
                }

                result.authorsOtherDevices?.let { otherDevicesAuthors ->
                    val lastTimestamp = otherDevicesAuthors.takeIf { it.isNotEmpty() }
                        ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
                    if (otherDevicesAuthors.isNotEmpty() && lastTimestamp != null) {
                        val authorsVo = otherDevicesAuthors.mapNotNull { it.toAuthorVo() }
                        authorsRepository.insertOrUpdateAuthorsInLocalDb(authorsVo)
                        authorsRepository.updateAuthorsTimestamp(
                            thisDeviceTimestamp = null,
                            otherDeviceTimestamp = lastTimestamp
                        )
                    }
                }

                result.booksCurrentDevice?.let { currentDevicesBooks ->
                    if (currentDevicesBooks.isNotEmpty()) {
                        val booksLocalDto =
                            currentDevicesBooks.mapNotNull { it.toVo()?.toLocalDto(userId) }
                        localBookInfoDataSource.addOrUpdateBooks(booksLocalDto, userId = userId)
                    }
                }

                result.authorsCurrentDevice?.let { otherDevicesAuthors ->
                    if (otherDevicesAuthors.isNotEmpty()) {
                        val authorsVo = otherDevicesAuthors.mapNotNull { it.toAuthorVo() }
                        authorsRepository.insertOrUpdateAuthorsInLocalDb(authorsVo)
                    }
                }
                success = true
            }

            response?.result?.currentDeviceBooksAndAuthorsAddedToServer?.let { result ->
                val resultBooks =
                    result.books?.mapNotNull { it.toVo()?.toLocalDto(userId) } ?: emptyList()
                val resultAuthors = result.authors?.mapNotNull { it.toAuthorVo() } ?: emptyList()
                localBookInfoDataSource.addOrUpdateBooks(resultBooks, userId = userId)
                authorsRepository.insertOrUpdateAuthorsInLocalDb(resultAuthors)
                success = true
            }

            response?.result?.currentDeviceBookLastTimestamp?.let {
                val booksTimestamp = localBookInfoDataSource.getBookTimestamp(userId = userId)
                localBookInfoDataSource.updateBookTimestamp(
                    booksTimestamp.copy(thisDeviceTimestamp = it)
                )
            }

            response?.result?.currentDeviceAuthorLastTimestamp?.let {
                authorsRepository.updateAuthorsTimestamp(
                    thisDeviceTimestamp = it,
                    otherDeviceTimestamp = null
                )
            }
            return@withLock success
        }
    }

    private suspend fun getSynchronizeBody(userId: Long): SynchronizeBooksWithAuthorsRequest {
        val booksTimestamp = localBookInfoDataSource.getBookTimestamp(userId = userId)
        val authorTimestampVo = authorsRepository.getAuthorsTimestamp(userId)
        val books = localBookInfoDataSource.getNotSynchronizedBooks(userId)
            .map { it.toVo(null).toRemoteDto() }
        return SynchronizeBooksWithAuthorsRequest(
            booksThisDeviceTimestamp = booksTimestamp.thisDeviceTimestamp,
            booksOtherDevicesTimestamp = booksTimestamp.otherDevicesTimestamp,
            authorsThisDeviceTimestamp = authorTimestampVo.thisDeviceTimestamp,
            authorsOtherDevicesTimestamp = authorTimestampVo.otherDevicesTimestamp,
            books = books
        )
    }

    private suspend fun updateBooksTimestamp(timestamp: Long) {
        val lastTimestamp = localBookInfoDataSource.getBookTimestamp(appConfig.userId)
        val finalTimestamp: BookTimestampEntity = lastTimestamp.copy(
            thisDeviceTimestamp = timestamp
        )
        localBookInfoDataSource.updateBookTimestamp(finalTimestamp)
    }

}