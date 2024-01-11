package database

import app.cash.sqldelight.db.SqlDriver
import com.yourlibrary.database.AppDatabaseQueries
import di.database.DEFAULT_DB_NAME
import platform.Platform
import platform.isMobile
import sqldelight.com.yourlibrary.database.AppDatabase
import java.io.File


class SqlDelightDataSource(private val dbDriverFactory: DbDriverFactory) {
    private var driver: SqlDriver = dbDriverFactory.createDriver(null, true, null)
    private var database: AppDatabase = AppDatabase(driver)
    private var dbQuery: AppDatabaseQueries = database.appDatabaseQueries
    fun pathIsExist(platform: Platform): Boolean {
        return if (platform.isMobile()) true
        else {
            val pathItem = dbQuery.selectAllFilesInfo().executeAsList()
                .firstOrNull() //todo исправить если будет несколько бд
            val file = File(
                pathItem?.dbPath,
                DEFAULT_DB_NAME
            ) //todo если мы введем имя для дб нужно переименовывать
            if (file.exists()) {
                return true
            } else if (pathItem != null) {
                dbQuery.removeFileInfo(pathItem.dbPath)
            }
            return false
        }

    }

    fun createDbPath(path: String): Boolean {
        dbQuery.insertFileInfo(
            1,
            path,
            DEFAULT_DB_NAME
        ) //todo изменить defaultDBNAME если мы хотим своё название файла добавлять
        dbDriverFactory.createDriver(path, false, null)
        return true
    }

    fun getCurrentPath(): String = dbQuery.selectAllFilesInfo().executeAsList()[0].dbPath

}

