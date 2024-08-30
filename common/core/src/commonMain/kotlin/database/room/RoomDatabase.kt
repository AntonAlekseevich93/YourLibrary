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
import database.room.entities.AuthorEntity
import database.room.entities.AuthorsTimestampEntity
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity
import database.room.entities.ReviewAndRatingEntity
import database.room.entities.ReviewAndRatingTimestampEntity

@Database(
    entities = [
        BookEntity::class,
        BookTimestampEntity::class,
        AuthorEntity::class,
        AuthorsTimestampEntity::class,
        ReviewAndRatingEntity::class,
        ReviewAndRatingTimestampEntity::class,
    ], version = 8
)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBooksDao(): BooksDao
    abstract fun getBooksTimestampDao(): BookTimestampDao
    abstract fun getAuthorsDao(): AuthorsDao
    abstract fun getAuthorsTimestampDao(): AuthorsTimestampDao
    abstract fun getReviewAndRatingDao(): ReviewAndRatingDao
    abstract fun getReviewAndRatingTimestampDao(): ReviewAndRatingTimestampDao
}