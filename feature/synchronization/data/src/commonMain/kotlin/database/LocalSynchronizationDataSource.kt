package database

import database.room.RoomMainDataSource
import database.room.entities.notifications.UserNotificationsEntity


class LocalSynchronizationDataSource(
    roomDb: RoomMainDataSource,
) {
    private val dao = roomDb.userNotificationsDao

    suspend fun insertOrUpdate(userId: Int, deviceId: String, pushToken: String) {
        val notificationData = dao.getNotifications(userId, deviceId).firstOrNull()
        if (notificationData == null) {
            dao.insertNotification(
                UserNotificationsEntity(
                    userId = userId,
                    deviceId = deviceId,
                    pushToken = pushToken
                )
            )
        } else {
            dao.updateNotification(pushToken = pushToken, deviceId = deviceId, userId = userId)
        }
    }

    suspend fun getNotificationData(userId: Int, deviceId: String): UserNotificationsEntity? =
        dao.getNotifications(userId, deviceId).firstOrNull()
}