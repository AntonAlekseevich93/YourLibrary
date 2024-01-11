package database

import app.cash.sqldelight.db.SqlDriver
import di.PlatformConfiguration

expect class DbDriverFactory constructor(platformConfiguration: PlatformConfiguration) {
    fun createDriver(path: String?, isPathDb: Boolean, dbName: String?): SqlDriver
}