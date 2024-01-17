import main_models.settings.SettingsVo

interface SettingsRepository {
    suspend fun createAppSettingsFile(
        path: String,
        libraryName: String,
        themeName: String,
    )

    suspend fun getAppSettingsFromFile(
        path: String,
    ): SettingsVo?

    suspend fun updateLibraryNameInFile(path: String, oldName: String, newName: String)
    suspend fun getLibraryNameIfExist(path: String): String?

}