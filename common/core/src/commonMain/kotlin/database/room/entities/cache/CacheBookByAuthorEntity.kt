package database.room.entities.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.books.CacheShortBookByAuthorVo
import main_models.rest.books.BookShortRemoteDto

@Entity
data class CacheBookByAuthorEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("cacheLocalId") val localId: Long? = null,
    @SerialName("cacheBook")
    @Embedded
    val cacheBook: BookShortRemoteDto?,
    @SerialName("cacheAuthorId") val cacheAuthorId: String,
    @SerialName("cacheTimestamp") val cacheTimestamp: Long,
    @SerialName("cacheUserId") val cacheUserId: Int,
)

fun CacheBookByAuthorEntity.toCacheVo(): CacheShortBookByAuthorVo =
    CacheShortBookByAuthorVo(
        localId = localId,
        cacheBook = cacheBook,
        cacheAuthorId = cacheAuthorId,
        cacheTimestamp = cacheTimestamp,
        cacheUserId = cacheUserId
    )
