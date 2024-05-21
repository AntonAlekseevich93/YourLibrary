package database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import database.room.dao.BooksDao
import database.room.entities.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBooksDao(): BooksDao
}