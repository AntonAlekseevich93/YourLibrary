import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ktor.RemoteSynchronizationDataSource
import main_models.rest.authors.toAuthorVo
import main_models.rest.sync.SynchronizeBooksWithAuthorsRequest
import main_models.rest.sync.SynchronizeUserDataRequest

class SynchronizationRepositoryImpl(
    private val remoteSynchronizationDataSource: RemoteSynchronizationDataSource,
    private val bookRepository: BookInfoRepository,
    private val authorsRepository: AuthorsRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : SynchronizationRepository {
    private val mutex = Mutex()

    override suspend fun synchronizeUserData(): Boolean {
        return if (appConfig.isAuth) {
            synchronize()
        } else false
    }

    private suspend fun synchronize(): Boolean {
        return mutex.withLock {
            val userId = appConfig.userId
            val response = remoteSynchronizationDataSource.synchronizeUserData(
                body = getSynchronizeBody(userId)
            )
            var success = false
            response?.result?.booksWithAuthorsContent?.missingBooksAndAuthorsFromServer?.let { result ->
                result.booksOtherDevices?.let { otherDevicesBooks ->
                    val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
                    val lastTimestamp = otherDevicesBooks.takeIf { it.isNotEmpty() }
                        ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
                    if (otherDevicesBooks.isNotEmpty() && lastTimestamp != null) {
                        bookRepository.addOrUpdateLocalBooks(otherDevicesBooks, userId = userId)
                        bookRepository.updateBookTimestamp(
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
                        bookRepository.addOrUpdateLocalBooks(currentDevicesBooks, userId = userId)
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

            response?.result?.booksWithAuthorsContent?.currentDeviceBooksAndAuthorsAddedToServer?.let { result ->
                val resultAuthors = result.authors?.mapNotNull { it.toAuthorVo() } ?: emptyList()
                bookRepository.addOrUpdateLocalBooks(result.books.orEmpty(), userId = userId)
                authorsRepository.insertOrUpdateAuthorsInLocalDb(resultAuthors)
                success = true
            }

            response?.result?.booksWithAuthorsContent?.currentDeviceBookLastTimestamp?.let {
                val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
                bookRepository.updateBookTimestamp(
                    booksTimestamp.copy(thisDeviceTimestamp = it)
                )
            }

            response?.result?.booksWithAuthorsContent?.currentDeviceAuthorLastTimestamp?.let {
                authorsRepository.updateAuthorsTimestamp(
                    thisDeviceTimestamp = it,
                    otherDeviceTimestamp = null
                )
            }
            return@withLock success
        }
    }

    private suspend fun getSynchronizeBody(userId: Long): SynchronizeUserDataRequest {
        val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
        val authorTimestampVo = authorsRepository.getAuthorsTimestamp(userId)
        val books = bookRepository.getNotSynchronizedBooks(userId)
        return SynchronizeUserDataRequest(
            booksWithAuthors = SynchronizeBooksWithAuthorsRequest(
                booksThisDeviceTimestamp = booksTimestamp.thisDeviceTimestamp,
                booksOtherDevicesTimestamp = booksTimestamp.otherDevicesTimestamp,
                authorsThisDeviceTimestamp = authorTimestampVo.thisDeviceTimestamp,
                authorsOtherDevicesTimestamp = authorTimestampVo.otherDevicesTimestamp,
                books = books
            ),
            reviewsAndRatings = null
        )
    }
}