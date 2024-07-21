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

    suspend fun insertOrUpdateAuthor(author: AuthorEntity, userId: Long) {
        val authorFromDb = authorsDao.getAuthorByAuthorId(author.id, userId = userId).firstOrNull()
        if (authorFromDb == null) {
            authorsDao.insertAuthor(author)
        } else {
            val resultAuthor = author.copy(localId = authorFromDb.localId)
            authorsDao.updateAuthor(resultAuthor)
        }
    }

    suspend fun getAuthorsTimestamp(userId: Long): AuthorsTimestampEntity {
        val timestamp = authorsTimestampDao.getTimestamp(userId).firstOrNull()
        return timestamp ?: createEmptyTimestamp(userId)
    }

    suspend fun updateAuthorsTimestamp(authorsTimestamp: AuthorsTimestampEntity) {
        authorsTimestampDao.insertOrUpdateTimestamp(authorsTimestamp)
    }

    suspend fun getNotSynchronizedAuthors(userId: Long): List<AuthorEntity> {
        val timestamp = getAuthorsTimestamp(userId)
        return authorsDao.getNotSynchronizedAuthors(timestamp.thisDeviceTimestamp, userId = userId)
    }

    suspend fun createAuthorIfNotExist(author: AuthorEntity, userId: Long) {
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

    private suspend fun createEmptyTimestamp(userId: Long): AuthorsTimestampEntity {
        val timestamp = AuthorsTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0
        )
        authorsTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }

}