import main_models.BookVo
import main_models.service_development.BookWithServiceDevelopment
import main_models.service_development.ServiceDevelopmentBooksTimestampVo
import main_models.service_development.UserServiceDevelopmentBookVo

interface ServiceDevelopmentRepository {
    suspend fun getNotSynchronizedServiceDevelopmentBooks(userId: Int): List<UserServiceDevelopmentBookVo>
    suspend fun getServiceDevelopmentBooksTimestamp(userId: Int): ServiceDevelopmentBooksTimestampVo
    suspend fun addOrUpdateLocalServiceDevelopmentBooksWhenSync(
        serviceDevelopmentBooks: List<UserServiceDevelopmentBookVo>,
        userId: Int
    )

    suspend fun updateServiceDevelopmentBooksTimestamp(timestamp: ServiceDevelopmentBooksTimestampVo)
    suspend fun updateServiceDevelopmentBooksTimestamp(
        thisDeviceTimestamp: Long?,
        otherDevicesTimestamp: Long?,
    )

    suspend fun getBookWithServiceDevelopment(book: BookVo): BookWithServiceDevelopment?
}