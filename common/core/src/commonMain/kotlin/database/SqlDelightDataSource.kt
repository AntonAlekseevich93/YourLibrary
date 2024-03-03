package database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import com.yourlibrary.database.AppDatabaseQueries
import com.yourlibrary.database.FilesInfo
import di.database.DEFAULT_DB_NAME_POSTFIX
import di.database.DEFAULT_DB_NAME_PREFIX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.path.PathInfoDto
import main_models.path.PathInfoVo
import platform.Platform
import platform.isDesktop
import platform.isMobile
import sqldelight.com.yourlibrary.database.AppDatabase
import java.io.File

const val DEBUG_PREFIX = "debug_"

class SqlDelightDataSource(
    private val platform: Platform,
    private val dbDriverFactory: DbDriverFactory,
) {
    private var pathDriver: SqlDriver = dbDriverFactory.createDriver(null, true, null)
    private var pathDatabase: AppDatabase = AppDatabase(pathDriver)
    private var pathDbQuery: AppDatabaseQueries = pathDatabase.appDatabaseQueries

    //todo сломается когда удалим бд. т.к .пути не будет
    private val pathDb: String
        get() = getCurrentPath()
    private lateinit var appDriver: SqlDriver
    private lateinit var appDatabase: AppDatabase
    lateinit var appQuery: AppDatabaseQueries
    private var appDbIsNotInitialized = true

    fun isAppDbIsNotInitialized() = appDbIsNotInitialized

    fun initializeAppDatabase() {
        appDriver = if (platform.isDesktop()) {
            dbDriverFactory.createDriver(pathDb, false, null)
        } else {
            pathDriver
        }

        appDatabase = if (platform.isDesktop()) {
            AppDatabase(appDriver)
        } else {
            pathDatabase
        }

        appQuery = if (platform.isDesktop()) {
            appDatabase.appDatabaseQueries
        } else {
            pathDbQuery
        }

        appDbIsNotInitialized = false
    }

    fun isPathIsExist(platform: Platform): Boolean {
        return if (platform.isMobile()) true
        else {
            val pathItem = pathDbQuery.selectAllFilesInfo().executeAsList()
                .find { it.isSelected.toInt() == DB_IS_SELECTED }
                ?: return false
            val file = File(
                pathItem.dbPath,
                pathItem.dbName
            )
            if (file.exists()) {
                return true
            } else {
                /**the situation is when there is a path in the database,
                but the file was deleted by the user or moved**/
                pathDbQuery.removeFileInfo(pathItem.dbPath)
            }
            return false
        }
    }

    fun createDbPath(path: String, libraryName: String): Int? {
        val lastId = pathDbQuery.selectAllFilesInfo().executeAsList().maxOfOrNull { it.id }
        val resultId = if (lastId != null) lastId + 1 else 1
        val dbName = if (platform.isDebug) {
            if (resultId.toInt() == 1) {
                DEBUG_PREFIX + DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX
            } else {
                "$DEBUG_PREFIX${DEFAULT_DB_NAME_PREFIX}_${resultId.toInt()}$DEFAULT_DB_NAME_POSTFIX"
            }
        } else {
            if (resultId.toInt() == 1) {
                DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX
            } else {
                "${DEFAULT_DB_NAME_PREFIX}_${resultId.toInt()}$DEFAULT_DB_NAME_POSTFIX"
            }
        }
        try {
            pathDbQuery.insertFileInfo(
                id = resultId,
                dbPath = path,
                dbName = dbName,
                libraryName = libraryName,
                isSelected = DB_IS_SELECTED.toLong()
            )
            dbDriverFactory.createDriver(path, false, dbName)
        } catch (_: Throwable) {
            return null
        }

        return resultId.toInt()
    }

    suspend fun getAllPathInfo(): Flow<PathInfoVo?> =
        pathDbQuery.selectAllFilesInfo().asFlow().mapToOneOrNull(Dispatchers.IO).map {
            it?.toPathInfoDto()?.toVo()
        }

    fun renamePath(id: Int, newPath: String, newName: String) {
        pathDbQuery.updateSelectedPathInfo(
            dbPath = newPath,
            libraryName = newName,
            id = id.toLong()
        )
        initializeAppDatabase()
    }

    fun setPathAsSelected(id: Int) {
        pathDbQuery.setAllPathAsNonSelected()
        pathDbQuery.setPathAsSelected(id.toLong())
    }

    private fun getCurrentPath(): String = pathDbQuery.selectAllFilesInfo().executeAsList()
        .firstOrNull { it.isSelected == DB_IS_SELECTED.toLong() }?.dbPath ?: ""

    suspend fun getSelectedPathInfo(): Flow<PathInfoDto?> =
        pathDbQuery.getSelectedPathInfo(0).asFlow().mapToOneOrNull(Dispatchers.IO)
            .map { it?.toPathInfoDto() }

    private fun FilesInfo.toPathInfoDto() = PathInfoDto(
        id = id.toInt(),
        path = dbPath,
        libraryName = libraryName,
        dbName = dbName,
        isSelected = isSelected.toInt()
    )

    private fun PathInfoDto.toVo(): PathInfoVo? {
        return if (id == null || path == null || libraryName == null || dbName == null || isSelected == null) null
        else PathInfoVo(
            id = id!!,
            path = path!!,
            libraryName = libraryName!!,
            dbName = dbName!!,
            isSelected = isSelected == DB_IS_SELECTED
        )
    }

    companion object {
        const val DB_IS_SELECTED = 0
        const val DB_IS_NOT_SELECTED = 1
    }
}

