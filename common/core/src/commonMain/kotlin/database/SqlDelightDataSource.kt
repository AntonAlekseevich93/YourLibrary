package database

import app.cash.sqldelight.db.SqlDriver
import com.yourlibrary.database.AppDatabaseQueries
import di.database.DEFAULT_DB_NAME
import platform.Platform
import platform.isMobile
import sqldelight.com.yourlibrary.database.AppDatabase
import java.io.File


class SqlDelightDataSource(private val dbDriverFactory: DbDriverFactory) {
    private var pathDriver: SqlDriver = dbDriverFactory.createDriver(null, true, null)
    private var pathDatabase: AppDatabase = AppDatabase(pathDriver)
    private var pathDbQuery: AppDatabaseQueries = pathDatabase.appDatabaseQueries

    //todo сломается когда удалим бд. т.к .пути не будет
    private val pathDb = getCurrentPath()
    private var appDriver = dbDriverFactory.createDriver(pathDb, false, null)
    val appDatabase = AppDatabase(appDriver)
    val appQuery = appDatabase.appDatabaseQueries

    fun pathIsExist(platform: Platform): Boolean {
        return if (platform.isMobile()) true
        else {
            val pathItem = pathDbQuery.selectAllFilesInfo().executeAsList()
                .firstOrNull() //todo исправить если будет несколько бд
            val file = File(
                pathItem?.dbPath,
                DEFAULT_DB_NAME
            ) //todo если мы введем имя для дб нужно переименовывать
            if (file.exists()) {
                return true
            } else if (pathItem != null) {
                pathDbQuery.removeFileInfo(pathItem.dbPath)
            }
            return false
        }

    }

    fun createDbPath(path: String): Boolean {
        pathDbQuery.insertFileInfo(
            1,
            path,
            DEFAULT_DB_NAME
        ) //todo изменить defaultDBNAME если мы хотим своё название файла добавлять
        dbDriverFactory.createDriver(path, false, null)
        return true
    }

    private fun getCurrentPath(): String =
        pathDbQuery.selectAllFilesInfo().executeAsList()[0].dbPath
}

