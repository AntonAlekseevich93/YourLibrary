package database

import database.room.RoomMainDataSource
import database.room.entities.UserServiceDevelopmentBookEntity
import database.room.entities.UserServiceDevelopmentBookTimestampEntity
import platform.PlatformInfoData

class LocalServiceDevelopmentDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val serviceDevelopmentBooksTimestampDao = roomDb.userServiceDevelopmentBookTimestampDao
    private val serviceDevelopmentBookDao = roomDb.userServiceDevelopmentBookDao

    suspend fun getNotSynchronizedServiceDevelopmentBooks(userId: Int): List<UserServiceDevelopmentBookEntity> {
        val timestamp = getServiceDevelopmentBooksTimestamp(userId)
        return serviceDevelopmentBookDao.getNotSynchronizedUserServiceDevelopmentBook(
            timestamp = timestamp.thisDeviceTimestamp,
            userId = userId
        )
    }

    suspend fun getServiceDevelopmentBooksTimestamp(userId: Int) =
        serviceDevelopmentBooksTimestampDao.getTimestamp(userId).firstOrNull()
            ?: createEmptyTimestamp(userId)

    suspend fun updateServiceDevelopmentBooksTimestamp(timestamp: UserServiceDevelopmentBookTimestampEntity) {
        serviceDevelopmentBooksTimestampDao.insertOrUpdateTimestamp(timestamp)
    }

    suspend fun addOrUpdateLocalServiceDevelopmentBooksWhenSync(
        serviceDevelopmentBooksEntities: List<UserServiceDevelopmentBookEntity>,
        userId: Int
    ) {
        serviceDevelopmentBooksEntities.forEach { item ->
            val existedServiceBook = serviceDevelopmentBookDao.getUserServiceDevelopmentBook(
                userId = userId,
                userBookId = item.userBookId
            ).firstOrNull()
            if (existedServiceBook == null) {
                serviceDevelopmentBookDao.insertUserServiceDevelopmentBook(item)
            } else {
                serviceDevelopmentBookDao.insertUserServiceDevelopmentBook(item.copy(localId = existedServiceBook.localId))
            }
        }
    }

    suspend fun getServiceDevelopmentByBookId(
        bookId: String,
        userId: Int
    ): UserServiceDevelopmentBookEntity? =
        serviceDevelopmentBookDao.getUserServiceDevelopmentBook(
            userBookId = bookId,
            userId = userId
        ).firstOrNull()

    private suspend fun createEmptyTimestamp(userId: Int): UserServiceDevelopmentBookTimestampEntity {
        val timestamp = UserServiceDevelopmentBookTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0,
        )
        serviceDevelopmentBooksTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }


}
