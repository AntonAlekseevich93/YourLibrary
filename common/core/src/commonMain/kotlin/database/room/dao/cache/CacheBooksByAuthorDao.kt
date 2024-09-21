package database.room.dao.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import database.room.entities.cache.CacheBookByAuthorEntity

@Dao
interface CacheBooksByAuthorDao {
    @Insert
    suspend fun insertBook(book: CacheBookByAuthorEntity)

    @Query("SELECT * FROM CacheBookByAuthorEntity WHERE cacheAuthorId = :authorId and cacheUserId =:userId")
    suspend fun getCacheBooksByAuthor(
        authorId: String,
        userId: Long,
    ): List<CacheBookByAuthorEntity>

    @Query("DELETE FROM CacheBookByAuthorEntity WHERE cacheAuthorId =:authorId and cacheUserId =:userId")
    suspend fun deleteCacheByAuthor(
        authorId: String,
        userId: Long
    )

}