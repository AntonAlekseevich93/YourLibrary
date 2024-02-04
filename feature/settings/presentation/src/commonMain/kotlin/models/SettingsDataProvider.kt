package models

interface SettingsDataProvider {
    fun createAppSettingsFile(path: String, libraryName: String, themeName: String)
    fun updateLibraryNameInFile(path: String, oldName: String, newName: String)
    suspend fun getLibraryNameIfExist(path: String): String?
}