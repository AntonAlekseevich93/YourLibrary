package database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import database.room.entities.AuthorEntity

@Dao
interface AuthorsDao {
    @Insert
    suspend fun insertAuthor(author: AuthorEntity)

    @Update
    suspend fun updateAuthor(author: AuthorEntity)

    @Query("SELECT * FROM AuthorEntity WHERE id = :authorId AND userId = :userId")
    suspend fun getAuthorByAuthorId(authorId: String, userId: Int): List<AuthorEntity>

    @Query("SELECT * FROM AuthorEntity WHERE timestampOfUpdating > :timestamp AND userId = :userId")
    suspend fun getNotSynchronizedAuthors(timestamp: Long, userId: Int): List<AuthorEntity>
}