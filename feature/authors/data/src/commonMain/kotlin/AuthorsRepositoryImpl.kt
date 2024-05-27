import database.LocalAuthorsDataSource
import database.room.entities.AuthorsTimestampEntity
import database.room.entities.toLocalDto
import ktor.RemoteAuthorsDataSource
import main_models.AuthorVo

class AuthorsRepositoryImpl(
    private val remoteAuthorsDataSource: RemoteAuthorsDataSource,
    private val localAuthorsDataSource: LocalAuthorsDataSource,
    private val appConfig: AppConfig,
) : AuthorsRepository {

    override suspend fun updateLocalAuthor(author: AuthorVo) {
        val authorDto = author.toLocalDto(appConfig.userId)
        localAuthorsDataSource.insertOrUpdateAuthor(authorDto)
    }

    override suspend fun updateAuthorsTimestamp(timestamp: Long) {
        val lastTimestamp = localAuthorsDataSource.getAuthorsTimestamp(appConfig.userId)
        val finalTimestamp: AuthorsTimestampEntity = lastTimestamp?.copy(
            thisDeviceTimestamp = timestamp
        ) ?: AuthorsTimestampEntity(
            userId = appConfig.userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = timestamp
        )
        localAuthorsDataSource.updateAuthorsTimestamp(finalTimestamp)
    }
}