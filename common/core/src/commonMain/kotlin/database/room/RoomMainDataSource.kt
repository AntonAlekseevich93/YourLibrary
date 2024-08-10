package database.room

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.room.dao.AuthorsDao
import database.room.dao.AuthorsTimestampDao
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import database.room.dao.ReviewAndRatingDao
import database.room.dao.ReviewAndRatingTimestampDao
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
        db.getBooksTimestampDao()
    }

    val authorsDao: AuthorsDao by lazy {
        db.getAuthorsDao()
    }

    val authorsTimestampDao: AuthorsTimestampDao by lazy {
        db.getAuthorsTimestampDao()
    }

    val reviewAndRatingDao: ReviewAndRatingDao by lazy {
        db.getReviewAndRatingDao()
    }

    val reviewAndRatingTimestampDao: ReviewAndRatingTimestampDao by lazy {
        db.getReviewAndRatingTimestampDao()
    }

    private fun getRoomDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ): AppDatabase {
        return builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(true)//todo fix
            .build()
    }
}