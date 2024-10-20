package database.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import database.room.dao.AuthorsDao
import database.room.dao.AuthorsTimestampDao
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import database.room.dao.ReviewAndRatingDao
import database.room.dao.ReviewAndRatingTimestampDao
import database.room.dao.user.UserGoalsInYearsDao
import database.room.dao.user.UserNotificationsDao
import database.room.dao.user.UserServiceDevelopmentBookDao
import database.room.dao.user.UserServiceDevelopmentBookTimestampDao
import database.room.dao.user.UsersDao
import database.room.dao.cache.CacheBooksByAuthorDao
import database.room.dao.cache.CacheReviewAndRatingDao
import database.room.dao.user.UserTimestampDao
import database.room.entities.AuthorEntity
import database.room.entities.AuthorsTimestampEntity
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity
import database.room.entities.ReviewAndRatingEntity
import database.room.entities.ReviewAndRatingTimestampEntity
import database.room.entities.user.UserEntity
import database.room.entities.UserServiceDevelopmentBookEntity
import database.room.entities.UserServiceDevelopmentBookTimestampEntity
import database.room.entities.cache.CacheBookByAuthorEntity
import database.room.entities.cache.CacheReviewAndRatingEntity
import database.room.entities.notifications.UserNotificationsEntity
import database.room.entities.user.UserGoalInYearEntity
import database.room.entities.user.UserTimestampEntity

@Database(
    entities = [
        BookEntity::class,
        BookTimestampEntity::class,
        AuthorEntity::class,
        UserEntity::class,
        UserTimestampEntity::class,
        AuthorsTimestampEntity::class,
        ReviewAndRatingEntity::class,
        ReviewAndRatingTimestampEntity::class,
        CacheBookByAuthorEntity::class,
        CacheReviewAndRatingEntity::class,
        UserNotificationsEntity::class,
        UserServiceDevelopmentBookEntity::class,
        UserServiceDevelopmentBookTimestampEntity::class,
        UserGoalInYearEntity::class,
    ], version = 1
)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUsersDao(): UsersDao
    abstract fun getUserTimestampDao(): UserTimestampDao
    abstract fun getBooksDao(): BooksDao
    abstract fun getBooksTimestampDao(): BookTimestampDao
    abstract fun getAuthorsDao(): AuthorsDao
    abstract fun getAuthorsTimestampDao(): AuthorsTimestampDao
    abstract fun getReviewAndRatingDao(): ReviewAndRatingDao
    abstract fun getReviewAndRatingTimestampDao(): ReviewAndRatingTimestampDao
    abstract fun getCacheBooksByAuthorDao(): CacheBooksByAuthorDao
    abstract fun getCacheReviewAndRatingDao(): CacheReviewAndRatingDao
    abstract fun getUserNotificationsDao(): UserNotificationsDao
    abstract fun getUserServiceDevelopmentBookDao(): UserServiceDevelopmentBookDao
    abstract fun getUserServiceDevelopmentBookTimestampDao(): UserServiceDevelopmentBookTimestampDao
    abstract fun getUserGoalInYearDao(): UserGoalsInYearsDao
}