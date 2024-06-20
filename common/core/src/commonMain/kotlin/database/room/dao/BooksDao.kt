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
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE BookEntity SET readingStatus = :readingStatus WHERE bookId = :bookId")
    suspend fun updateBookReadingStatus(readingStatus: String, bookId: String)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("SELECT * FROM BookEntity")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE bookId = :bookId")
    suspend fun getBookByRoomId(bookId: String): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE bookId = :bookId")
    suspend fun getBookStatusByBookId(bookId: String): List<BookEntity>
}