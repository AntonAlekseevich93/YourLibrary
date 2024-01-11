package database

import sqldelight.com.yourlibrary.database.AppDatabase

class LocalShelfDataSource(dbDriverFactory: DbDriverFactory) {
    private val driver = dbDriverFactory.createDriver(null, false, null) //todo передавать в параметры класса путь чтобы не создавалась новая бд
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    fun test() = "HeLlo Man"
}