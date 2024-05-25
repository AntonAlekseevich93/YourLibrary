package database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import database.room.dao.BookTimestampDao
import database.room.dao.BooksDao
import database.room.entities.BookEntity
import database.room.entities.BookTimestampEntity

@Database(
    entities = [
        BookEntity::class,
        BookTimestampEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBooksDao(): BooksDao
    abstract fun getTimestampDao(): BookTimestampDao
}