import kotlinx.coroutines.flow.Flow
import main_models.path.PathInfoVo
import platform.Platform
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ApplicationInteractor(
    private val repository: ApplicationRepository,
) {

    suspend fun getAllPathInfo(): Flow<PathInfoVo?> = repository.getAllPathInfo()

    fun isPathIsExist(platform: Platform): Boolean = repository.isPathIsExist(platform)

    fun isAppDbIsNotInitialized(): Boolean = repository.isAppDbIsNotInitialized()

    fun initializeAppDatabase() {
        repository.initializeAppDatabase()
    }

    fun setPathAsSelected(id: Int) {
        repository.setPathAsSelected(id)
    }

    fun createDbPath(path: String, libraryName: String): Int? {
        val id = repository.createDbPath(path, libraryName)
        if (isAppDbIsNotInitialized()) {
            initializeAppDatabase()
        }
        return id
    }

    suspend fun getPathByOs(path: String): String {
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        return if (path.last() == osDivider.first()) path else "$path$osDivider"
    }

    fun renamePath(pathInfo: PathInfoVo, newName: String): String {
        val pathString = pathInfo.path.replace(pathInfo.libraryName, newName)

        try {
            val oldPath = Paths.get(pathInfo.path)
            val newPath = Paths.get(pathString)
            Files.move(
                oldPath,
                newPath,
                StandardCopyOption.REPLACE_EXISTING
            )
            repository.renamePath(pathInfo.id, pathString, newName)
        } catch (_: Throwable) {
            //todo log
        }

        return pathString
    }

    fun createFolderAndGetPath(path: String, name: String): String? = try {
        File(path, name).mkdir()
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        val selectedPathResult =
            if (path.last() == osDivider.first()) path else "$path$osDivider"
        selectedPathResult + name + osDivider
    } catch (_: Exception) {
        null
    }

}