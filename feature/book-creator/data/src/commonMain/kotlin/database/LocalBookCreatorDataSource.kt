package database

import sqldelight.com.yourlibrary.database.AppDatabase

class LocalBookCreatorDataSource(
    dbDriverFactory: DbDriverFactory,
    pathDatabase: SqlDelightDataSource
) {

    private val pathDb = pathDatabase.getCurrentPath()
    private val driver = dbDriverFactory.createDriver(pathDb, false, null)
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

}