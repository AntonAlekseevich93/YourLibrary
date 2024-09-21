package database.room.entities.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.books.BookShortDtoCache

@Entity
data class CacheBookByAuthorEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("cacheLocalId") val localId: Long? = null,

    @SerialName("cacheBook")
    @Embedded
    val cacheBook: BookShortDtoCache,
    @SerialName("cacheAuthorId") val cacheAuthorId: String,
    @SerialName("cacheTimestamp") val cacheTimestamp: String,
)