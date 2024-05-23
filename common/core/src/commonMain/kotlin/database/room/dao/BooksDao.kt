package database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import database.room.entities.BookEntity

@Dao
interface BooksDao {
    @Insert
    suspend fun insertBook(book: BookEntity): Long

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE BookEntity SET readingStatus = :readingStatus WHERE roomId = :roomId")
    suspend fun updateBookReadingStatus(readingStatus: String, roomId: Long)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE roomId = :roomId")
    suspend fun getBookByRoomId(roomId: Long): List<BookEntity>

}