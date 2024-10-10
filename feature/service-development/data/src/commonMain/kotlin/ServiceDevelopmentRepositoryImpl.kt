import database.LocalServiceDevelopmentDataSource
import database.room.entities.toEntity
import database.room.entities.toVo
import ktor.RemoteServiceDevelopmentDataSource
import main_models.service_development.ServiceDevelopmentBooksTimestampVo
import main_models.service_development.UserServiceDevelopmentBookVo
import platform.PlatformInfoData

class ServiceDevelopmentRepositoryImpl(
    private val localServiceDevelopmentDataSource: LocalServiceDevelopmentDataSource,
    private val remoteServiceDevelopmentDataSource: RemoteServiceDevelopmentDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
    private val platformInfo: PlatformInfoData,
) : ServiceDevelopmentRepository {

    override suspend fun getNotSynchronizedServiceDevelopmentBooks(userId: Int) =
        localServiceDevelopmentDataSource.getNotSynchronizedServiceDevelopmentBooks(userId)
            .mapNotNull { it.toVo() }

    override suspend fun getServiceDevelopmentBooksTimestamp(userId: Int): ServiceDevelopmentBooksTimestampVo =
        localServiceDevelopmentDataSource.getServiceDevelopmentBooksTimestamp(userId).toVo()


    override suspend fun addOrUpdateLocalServiceDevelopmentBooksWhenSync(
        serviceDevelopmentBooks: List<UserServiceDevelopmentBookVo>,
        userId: Int
    ) {
        val serviceDevelopmentBooksEntity = serviceDevelopmentBooks.map { it.toEntity() }
        localServiceDevelopmentDataSource.addOrUpdateLocalServiceDevelopmentBooksWhenSync(
            serviceDevelopmentBooksEntity,
            userId = userId
        )
    }

    override suspend fun updateServiceDevelopmentBooksTimestamp(timestamp: ServiceDevelopmentBooksTimestampVo) {
        localServiceDevelopmentDataSource.updateServiceDevelopmentBooksTimestamp(timestamp.toEntity())
    }

    override suspend fun updateServiceDevelopmentBooksTimestamp(
        thisDeviceTimestamp: Long?,
        otherDevicesTimestamp: Long?,
    ) {
        val userId = appConfig.userId
        val timestamp = getServiceDevelopmentBooksTimestamp(userId)
        val newTimestamp = timestamp.copy(
            otherDevicesTimestamp = otherDevicesTimestamp ?: timestamp.otherDevicesTimestamp,
            thisDeviceTimestamp = thisDeviceTimestamp ?: timestamp.otherDevicesTimestamp,
        )
        localServiceDevelopmentDataSource.updateServiceDevelopmentBooksTimestamp(newTimestamp.toEntity())
    }

}