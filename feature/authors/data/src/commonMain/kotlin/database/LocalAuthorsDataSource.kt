package database

import database.room.RoomMainDataSource
import database.room.entities.AuthorEntity
import database.room.entities.AuthorsTimestampEntity
import platform.PlatformInfoData

class LocalAuthorsDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource
) {
    private val authorsDao = roomDb.authorsDao
    private val authorsTimestampDao = roomDb.authorsTimestampDao

    suspend fun insertOrUpdateAuthor(author: AuthorEntity, userId: Int) {
        val authorFromDb = authorsDao.getAuthorByAuthorId(author.id, userId = userId).firstOrNull()
        if (authorFromDb == null) {
            authorsDao.insertAuthor(author)
        } else {
            val resultAuthor = author.copy(localId = authorFromDb.localId)
            authorsDao.updateAuthor(resultAuthor)
        }
    }

    suspend fun getAuthorsTimestamp(userId: Int): AuthorsTimestampEntity {
        val timestamp = authorsTimestampDao.getTimestamp(userId).firstOrNull()
        return timestamp ?: createEmptyTimestamp(userId)
    }

    suspend fun updateAuthorsTimestamp(authorsTimestamp: AuthorsTimestampEntity) {
        authorsTimestampDao.insertOrUpdateTimestamp(authorsTimestamp)
    }

    suspend fun getNotSynchronizedAuthors(userId: Int): List<AuthorEntity> {
        val timestamp = getAuthorsTimestamp(userId)
        return authorsDao.getNotSynchronizedAuthors(timestamp.thisDeviceTimestamp, userId = userId)
    }

    suspend fun createAuthorIfNotExist(author: AuthorEntity, userId: Int) {
        val isNotExist =
            authorsDao.getAuthorByAuthorId(authorId = author.id, userId = userId).isEmpty()
        val time = platformInfo.getCurrentTime().timeInMillis
        if (isNotExist) {
            authorsDao.insertAuthor(
                author.copy(
                    timestampOfCreating = time,
                    timestampOfUpdating = time
                )
            )
        }
    }

    suspend fun getAuthorById(authorId: String, userId: Int) =
        authorsDao.getAuthorByAuthorId(authorId = authorId, userId = userId)

    private suspend fun createEmptyTimestamp(userId: Int): AuthorsTimestampEntity {
        val timestamp = AuthorsTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0
        )
        authorsTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }

}