interface SynchronizationRepository {
    suspend fun synchronizeUserData(): Boolean
    suspend fun updateNotificationPushToken(pushToken: String)
}