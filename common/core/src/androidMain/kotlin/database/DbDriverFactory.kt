package database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import di.PlatformConfiguration
import di.database.PATH_DB_NAME
import sqldelight.com.yourlibrary.database.AppDatabase

actual class DbDriverFactory actual constructor(private val platformConfiguration: PlatformConfiguration) {
    actual fun createDriver(path: String?, isPathDb: Boolean, dbName: String?): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema,
            platformConfiguration.androidContext,
            PATH_DB_NAME
        )
    }
}