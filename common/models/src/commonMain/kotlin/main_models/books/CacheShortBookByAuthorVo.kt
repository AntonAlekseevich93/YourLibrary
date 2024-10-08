package main_models.books

import kotlinx.serialization.SerialName
import main_models.rest.books.BookShortRemoteDto

data class CacheShortBookByAuthorVo(
    @SerialName("cacheLocalId") val localId: Long? = null,
    @SerialName("cacheBook") val cacheBook: BookShortRemoteDto?,
    @SerialName("cacheAuthorId") val cacheAuthorId: String,
    @SerialName("cacheTimestamp") val cacheTimestamp: Long,
    @SerialName("cacheUserId") val cacheUserId: Int,
)

fun CacheShortBookByAuthorVo.toRemoteDto(): BookShortRemoteDto? {
    return if (cacheBook == null) null
    else BookShortRemoteDto(
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
        authorFirstName = cacheBook.authorFirstName,
        authorMiddleName = cacheBook.authorMiddleName,
        authorLastName = cacheBook.authorLastName,
    )
}