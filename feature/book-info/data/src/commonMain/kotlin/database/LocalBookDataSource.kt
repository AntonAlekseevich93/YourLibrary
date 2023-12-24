package database

import sqldelight.com.yourlibrary.database.AppDatabase

class LocalBookDataSource(dbDriverFactory: DbDriverFactory) {
    private val driver = dbDriverFactory.createDriver()
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    fun test() = "HeLlo Man"
}