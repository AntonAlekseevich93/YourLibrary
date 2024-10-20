package database.room

import androidx.room.RoomDatabase
import database.room.dao.AuthorsDao
import database.room.dao.AuthorsTimestampDao
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import database.room.dao.ReviewAndRatingDao
import database.room.dao.ReviewAndRatingTimestampDao
import database.room.dao.cache.CacheBooksByAuthorDao
import database.room.dao.cache.CacheReviewAndRatingDao
import database.room.dao.user.UserGoalsInYearsDao
import database.room.dao.user.UserNotificationsDao
import database.room.dao.user.UserServiceDevelopmentBookDao
import database.room.dao.user.UserServiceDevelopmentBookTimestampDao
import database.room.dao.user.UserTimestampDao
import database.room.dao.user.UsersDao
import kotlinx.coroutines.Dispatchers
import platform.Platform

class RoomMainDataSource(
    private val platform: Platform,
    private val roomDbBuilder: RoomDbBuilder,
) {
    private val driver = roomDbBuilder.createRoomBuilder()
    private val db = getRoomDatabase(driver)

    val usersDao: UsersDao by lazy {
        db.getUsersDao()
    }

    val userTimestampDao: UserTimestampDao by lazy {
        db.getUserTimestampDao()
    }

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

    val cacheBooksByAuthorDao: CacheBooksByAuthorDao by lazy {
        db.getCacheBooksByAuthorDao()
    }

    val cacheReviewAndRatingDao: CacheReviewAndRatingDao by lazy {
        db.getCacheReviewAndRatingDao()
    }

    val userNotificationsDao: UserNotificationsDao by lazy {
        db.getUserNotificationsDao()
    }

    val userServiceDevelopmentBookDao: UserServiceDevelopmentBookDao by lazy {
        db.getUserServiceDevelopmentBookDao()
    }

    val userServiceDevelopmentBookTimestampDao: UserServiceDevelopmentBookTimestampDao by lazy {
        db.getUserServiceDevelopmentBookTimestampDao()
    }

    val userGoalInYearDao: UserGoalsInYearsDao by lazy {
        db.getUserGoalInYearDao()
    }

    private fun getRoomDatabase(
        builder: RoomDatabase.Builder<AppDatabase>
    ): AppDatabase {
        val driverProvider = SqlDriverProvider()
        return builder
            .setDriver(driverProvider.provideDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(true)//todo fix
            .build()
    }
}