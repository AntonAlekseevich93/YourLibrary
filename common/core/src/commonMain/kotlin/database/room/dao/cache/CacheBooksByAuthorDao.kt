package database.room.dao.cache

import androidx.room.Dao
import androidx.room.Insert
import database.room.entities.cache.CacheBookByAuthorEntity

@Dao
interface CacheBooksByAuthorDao {
    @Insert
    suspend fun insertBook(book: CacheBookByAuthorEntity)


}