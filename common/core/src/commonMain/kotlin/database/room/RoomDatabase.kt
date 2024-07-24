package database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import database.room.dao.AuthorsDao
import database.room.dao.AuthorsTimestampDao
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import database.room.entities.AuthorEntity
import database.room.entities.AuthorsTimestampEntity
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity

@Database(
    entities = [
        BookEntity::class,
        BookTimestampEntity::class,
        AuthorEntity::class,
        AuthorsTimestampEntity::class,
    ], version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBooksDao(): BooksDao
    abstract fun getBooksTimestampDao(): BookTimestampDao
    abstract fun getAuthorsDao(): AuthorsDao
    abstract fun getAuthorsTimestampDao(): AuthorsTimestampDao
}