package database

import sqldelight.com.yourlibrary.database.AppDatabase

class LocalBookInfoDataSource(dbDriverFactory: DbDriverFactory) {
    private val driver = dbDriverFactory.createDriver(null, false, null)
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    fun test() = "HeLlo Man"
}