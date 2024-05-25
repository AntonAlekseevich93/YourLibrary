package database.room

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import kotlinx.coroutines.Dispatchers
import platform.Platform

class RoomMainDataSource(
    private val platform: Platform,
    private val roomDbBuilder: RoomDbBuilder,
) {
    private val driver = roomDbBuilder.createRoomBuilder()
    private val db = getRoomDatabase(driver)

    val booksDao: BooksDao by lazy {
        db.getBooksDao()
    }

    val bookTimestampDao: BookTimestampDao by lazy {
        db.getTimestampDao()
    }

    private fun getRoomDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ): AppDatabase {
        return builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}