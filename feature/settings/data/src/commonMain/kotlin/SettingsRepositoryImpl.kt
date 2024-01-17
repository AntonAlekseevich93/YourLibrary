import JsonUtils.Companion.decodeToObject
import JsonUtils.Companion.encodeToString
import database.LocalSettingsDataSource
import main_models.settings.SettingsVo
import models.SettingsDto
import models.toVo

class SettingsRepositoryImpl(
    private val localSettingsDataSource: LocalSettingsDataSource,
    private val fileManager: FileManager
) : SettingsRepository {

    override suspend fun createAppSettingsFile(
        path: String,
        libraryName: String,
        themeName: String
    ) {
        val settings = SettingsDto(
            libraryName = libraryName,
            currentTheme = themeName
        )
        try {
            val json = settings.encodeToString()
            fileManager.createFile(
                path = path,
                jsonString = json,
                fileName = APP_SETTINGS_FILE_NAME,
            )
        } catch (_: Throwable) {
            //todo log
        }
    }

    override suspend fun getAppSettingsFromFile(path: String): SettingsVo? {
        val response = fileManager.getFileAsJson(
            path = path,
            fileName = APP_SETTINGS_FILE_NAME
        )
        try {
            if (response.isSuccess && response.jsonString != null) {
                return response.jsonString!!.decodeToObject<SettingsDto>().toVo()
            }
        } catch (t: Throwable) {
            //todo log
        }
        return null
    }

    override suspend fun updateLibraryNameInFile(path: String, oldName: String, newName: String) {
        fileManager.replaceTextInFile(
            path = path,
            fileName = APP_SETTINGS_FILE_NAME,
            oldText = oldName,
            newText = newName
        )
    }

    override suspend fun getLibraryNameIfExist(path: String): String? =
        getAppSettingsFromFile(path)?.libraryName

    companion object {
        private const val APP_SETTINGS_FILE_NAME = "app-settings.json"
        private const val PROJECTS_FILE_NAME = "app-projects.json"
    }

}