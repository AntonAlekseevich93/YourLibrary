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
    @SerialName("cacheTimestamp") val cacheTimestamp: Long,
    @SerialName("cacheUserId") val cacheUserId: Long,
)

fun CacheBookByAuthorEntity.toBookShortDtoCache(): BookShortDtoCache =
    BookShortDtoCache(
        id = cacheBook.id,
        bookId = cacheBook.bookId,
        originalAuthorId = cacheBook.originalAuthorId,
        bookName = cacheBook.bookName,
        originalBookName = cacheBook.originalBookName,
        originalAuthorName = cacheBook.originalAuthorName,
        description = cacheBook.description,
        numbersOfPages = cacheBook.numbersOfPages,
        isbn = cacheBook.isbn,
        bookGenreId = cacheBook.bookGenreId,
        ageRestrictions = cacheBook.ageRestrictions,
        imageName = cacheBook.imageName,
        isRussian = cacheBook.isRussian,
        rawCoverUrl = cacheBook.rawCoverUrl,
        imageFolderId = cacheBook.imageFolderId,
        ratingValue = cacheBook.ratingValue,
        ratingCount = cacheBook.ratingCount,
        reviewCount = cacheBook.reviewCount,
        ratingSum = cacheBook.ratingSum,
        mainBookId = cacheBook.mainBookId,
        isMainBook = cacheBook.isMainBook,
        lang = cacheBook.lang,
        publicationYear = cacheBook.publicationYear,
    )
