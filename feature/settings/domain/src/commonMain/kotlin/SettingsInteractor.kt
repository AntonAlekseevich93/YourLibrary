class SettingsInteractor(
    private val repository: SettingsRepository,
    private val cacheManagerRepository: CacheManagerRepository,
) {
    suspend fun clearAllCache() {
        cacheManagerRepository.clearAllCache()
    }
}