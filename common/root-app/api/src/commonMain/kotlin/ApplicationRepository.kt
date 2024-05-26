import kotlinx.coroutines.flow.Flow
import main_models.path.PathInfoVo
import platform.Platform

interface ApplicationRepository {
    suspend fun getAllPathInfo(): Flow<PathInfoVo?>
    fun isPathIsExist(platform: Platform): Boolean
    fun isAppDbIsNotInitialized(): Boolean
    fun initializeAppDatabase()
    fun setPathAsSelected(id: Int)
    fun renamePath(id: Int, newPath: String, newName: String)
    fun createDbPath(path: String, libraryName: String): Int?
//    suspend fun getAllBooks(): Flow<List<BookItemVo>>
}