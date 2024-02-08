import database.LocalApplicationDataSource
import database.SqlDelightDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.BookItemVo
import main_models.local_models.toVo
import main_models.path.PathInfoVo
import platform.Platform

class ApplicationRepositoryImpl(
    private val rootDatabase: SqlDelightDataSource,
    private val db: LocalApplicationDataSource,
) : ApplicationRepository {

    override suspend fun getAllPathInfo(): Flow<PathInfoVo?> = rootDatabase.getAllPathInfo()

    override fun isPathIsExist(platform: Platform): Boolean = rootDatabase.isPathIsExist(platform)

    override fun isAppDbIsNotInitialized(): Boolean = rootDatabase.isAppDbIsNotInitialized()

    override fun initializeAppDatabase() {
        rootDatabase.initializeAppDatabase()
    }

    override fun setPathAsSelected(id: Int) {
        rootDatabase.setPathAsSelected(id)
    }

    override fun renamePath(id: Int, newPath: String, newName: String) {
        rootDatabase.renamePath(id, newPath, newName)
    }

    override fun createDbPath(path: String, libraryName: String): Int? =
        rootDatabase.createDbPath(path, libraryName)

    override suspend fun getAllBooks(): Flow<List<BookItemVo>> =
        db.getAllBooks().map { list -> list.map { item -> item.toVo() } }

}