package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import database.room.entities.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Insert
    suspend fun insertBook(book: BookEntity)

    @Query("DELETE FROM BookEntity")
    suspend fun deleteAllData()

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE BookEntity SET readingStatus = :readingStatus WHERE bookId = :bookId AND userId = :userId")
    suspend fun updateBookReadingStatus(readingStatus: String, bookId: String, userId: Int)

    @Query("SELECT * FROM BookEntity WHERE userId = :userId")
    suspend fun getAllBooks(userId: Int): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE userId = :userId")
    fun getAllBooksFlow(userId: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE userId = :userId and readingStatus = :readingStatus")
    fun getAllBooksWithStatus(userId: Int, readingStatus: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE readingStatus = :status AND userId =:userId")
    fun getAllBooksFlowByStatus(status: String, userId: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE bookId = :bookId AND userId = :userId")
    suspend fun getBookByBookId(bookId: String, userId: Int): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE bookId = :bookId AND userId = :userId")
    suspend fun getBookStatusByBookId(bookId: String, userId: Int): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE timestampOfUpdating > :timestamp AND userId = :userId")
    suspend fun getNotSynchronizedBooks(timestamp: Long, userId: Int): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE bookNameUppercase LIKE '%' || :name || '%'")
    suspend fun searchBookByName(name: String): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE localId = :bookLocalId and userId = :userId")
    fun getLocalBookByLocalId(bookLocalId: Long, userId: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE bookId = :bookId and userId = :userId")
    fun getLocalBookById(bookId: String, userId: Int): Flow<List<BookEntity>>
}