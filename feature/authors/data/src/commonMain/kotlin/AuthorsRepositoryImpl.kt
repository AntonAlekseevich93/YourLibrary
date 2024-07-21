import database.LocalAuthorsDataSource
import database.room.entities.toLocalDto
import database.room.entities.toVo
import ktor.RemoteAuthorsDataSource
import main_models.AuthorVo

class AuthorsRepositoryImpl(
    private val remoteAuthorsDataSource: RemoteAuthorsDataSource,
    private val localAuthorsDataSource: LocalAuthorsDataSource,
    private val appConfig: AppConfig,
) : AuthorsRepository {

    override suspend fun updateAuthorInLocalDb(author: AuthorVo) {
        val userId = appConfig.userId
        val authorDto = author.toLocalDto(userId)
        localAuthorsDataSource.insertOrUpdateAuthor(authorDto, userId = userId)
    }

    override suspend fun updateAuthorsTimestamp(
        thisDeviceTimestamp: Long?,
        otherDeviceTimestamp: Long?
    ) {
        val lastTimestamp = localAuthorsDataSource.getAuthorsTimestamp(appConfig.userId)
        lastTimestamp.copy(
            thisDeviceTimestamp = thisDeviceTimestamp ?: lastTimestamp.thisDeviceTimestamp,
            otherDevicesTimestamp = otherDeviceTimestamp ?: lastTimestamp.otherDevicesTimestamp
        ).let { finalTimestamp ->
            localAuthorsDataSource.updateAuthorsTimestamp(finalTimestamp)
        }
    }

    override suspend fun insertOrUpdateAuthorsInLocalDb(authors: List<AuthorVo>) {
        val userId = appConfig.userId
        authors.forEach { author ->
            localAuthorsDataSource.insertOrUpdateAuthor(author.toLocalDto(userId), userId = userId)
        }
    }

    override suspend fun getAuthorsTimestamp(userId: Long) =
        localAuthorsDataSource.getAuthorsTimestamp(userId).toVo()

    override suspend fun getNotSynchronizedAuthors(): List<AuthorVo> =
        localAuthorsDataSource.getNotSynchronizedAuthors(appConfig.userId).map { it.toVo() }

    override suspend fun createAuthorIfNotExist(author: AuthorVo) {
        val userId = appConfig.userId
        localAuthorsDataSource.createAuthorIfNotExist(author.toLocalDto(userId), userId = userId)
    }

}