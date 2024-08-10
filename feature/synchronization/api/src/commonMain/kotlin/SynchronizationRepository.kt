interface SynchronizationRepository {
    suspend fun synchronizeUserData(): Boolean
}