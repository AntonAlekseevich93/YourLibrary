import HttpParams.AUTHORS_DEVICE_LAST_TIMESTAMP
import HttpParams.AUTHORS_OTHER_DEVICES_LAST_TIMESTAMP
import HttpParams.BOOKS_DEVICE_LAST_TIMESTAMP
import HttpParams.BOOKS_OTHER_DEVICES_LAST_TIMESTAMP
import database.LocalBookInfoDataSource
import database.room.entities.BookTimestampEntity
import database.room.entities.toLocalDto
import ktor.RemoteBookInfoDataSource
import main_models.authors.AuthorTimestampVo
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toVo

class BookInfoRepositoryImpl(
    private val localBookInfoDataSource: LocalBookInfoDataSource,
    private val remoteBookInfoDataSource: RemoteBookInfoDataSource,
    private val authorsRepository: AuthorsRepository,
    private val appConfig: AppConfig,
) : BookInfoRepository {

    override suspend fun getAllRemoteBooksWithAuthorsByTimestamps(): Boolean {
        val userId = appConfig.userId
        val booksTimestamp = localBookInfoDataSource.getBookTimestamp(userId = userId)
        val authorTimestamp = authorsRepository.getAuthorsTimestamp(userId)
        val response = remoteBookInfoDataSource.getAllBooksAndAuthorsByTimestamp(
            params = getParamsByTimestamps(
                booksTimestamp = booksTimestamp,
                authorTimestampVo = authorTimestamp
            )
        )

        response?.result?.let { result ->
            result.booksOtherDevices.let { otherDevicesBooks ->
                val lastTimestamp = otherDevicesBooks.takeIf { it.isNotEmpty() }
                    ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
                if (otherDevicesBooks.isNotEmpty() && lastTimestamp != null) {
                    val booksLocalDto =
                        otherDevicesBooks.mapNotNull { it.toVo()?.toLocalDto(userId) }
                    localBookInfoDataSource.addBooks(booksLocalDto)
                    localBookInfoDataSource.updateBookTimestamp(
                        booksTimestamp.copy(
                            otherDevicesTimestamp = lastTimestamp
                        )
                    )
                }
            }

            result.authorsOtherDevices.let { otherDevicesAuthors ->
                val lastTimestamp = otherDevicesAuthors.takeIf { it.isNotEmpty() }
                    ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
                if (otherDevicesAuthors.isNotEmpty() && lastTimestamp != null) {
                    val authorsVo = otherDevicesAuthors.mapNotNull { it.toAuthorVo() }
                    authorsRepository.addAuthorsToLocalDb(authorsVo)
                    authorsRepository.updateThisDeviceAuthorsTimestamp(
                        thisDeviceTimestamp = null,
                        otherDeviceTimestamp = lastTimestamp
                    )
                }
            }
            return true
        }
        return false
    }

    private fun getParamsByTimestamps(
        booksTimestamp: BookTimestampEntity,
        authorTimestampVo: AuthorTimestampVo
    ): Map<String, String> = mapOf<String, String>(
        BOOKS_DEVICE_LAST_TIMESTAMP to booksTimestamp.thisDeviceTimestamp.toString(),
        BOOKS_OTHER_DEVICES_LAST_TIMESTAMP to booksTimestamp.otherDevicesTimestamp.toString(),
        AUTHORS_DEVICE_LAST_TIMESTAMP to authorTimestampVo.thisDeviceTimestamp.toString(),
        AUTHORS_OTHER_DEVICES_LAST_TIMESTAMP to authorTimestampVo.otherDevicesTimestamp.toString()
    )

}