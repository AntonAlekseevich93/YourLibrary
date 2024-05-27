package database

import database.room.RoomMainDataSource
import database.room.entities.AuthorEntity
import database.room.entities.AuthorsTimestampEntity

class LocalAuthorsDataSource(
    roomDb: RoomMainDataSource
) {
    private val authorsDao = roomDb.authorsDao
    private val authorsTimestampDao = roomDb.authorsTimestampDao

    suspend fun insertOrUpdateAuthor(author: AuthorEntity) {
        val authorFromDb = authorsDao.getAuthorByAuthorId(author.id).firstOrNull()
        if (authorFromDb == null) {
            authorsDao.insertAuthor(author)
        } else {
            authorsDao.updateAuthor(author)
        }
    }

    suspend fun getAuthorsTimestamp(userId: Long) =
        authorsTimestampDao.getTimestamp(userId).firstOrNull()

    suspend fun updateAuthorsTimestamp(authorsTimestamp: AuthorsTimestampEntity) {
        authorsTimestampDao.updateTimestamp(authorsTimestamp)
    }

}