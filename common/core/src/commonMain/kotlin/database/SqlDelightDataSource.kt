package database

import sqldelight.com.yourlibrary.database.AppDatabase

const val nameDb = "template.db"

class SqlDelightDataSource(dbDriverFactory: DbDriverFactory) {
    private val driver = dbDriverFactory.createDriver()
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

}

