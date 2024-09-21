package main_models.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.books.BookShortRemoteDto

@Serializable
data class BookShortDtoCache(
    @SerialName("id") val id: Int? = null,
    @SerialName("bookId") val bookId: String? = null,
    @SerialName("originalAuthorId") val originalAuthorId: String? = null,
    @SerialName("bookName") val bookName: String? = null,
    @SerialName("originalBookName") val originalBookName: String? = null,
    @SerialName("originalAuthorName") val originalAuthorName: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("coverUrl") val rawCoverUrl: String? = null,
    @SerialName("imageName") val imageName: String? = null,
    @SerialName("numbersOfPages") val numbersOfPages: Int? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("bookGenreId") val bookGenreId: Int? = null,
    @SerialName("ageRestrictions") val ageRestrictions: String? = null,
    @SerialName("isRussian") val isRussian: Boolean? = null,
    @SerialName("imageFolderId") val imageFolderId: Int? = null,
    @SerialName("ratingValue") val ratingValue: Double,
    @SerialName("ratingCount") val ratingCount: Int,
    @SerialName("reviewCount") val reviewCount: Int,
    @SerialName("ratingSum") val ratingSum: Int,
    @SerialName("mainBookId") val mainBookId: String,
    @SerialName("isMainBook") val isMainBook: Boolean,
    @SerialName("lang") val lang: String,
    @SerialName("publicationYear") val publicationYear: String?,
)

fun BookShortRemoteDto.toCacheShortBook(): BookShortDtoCache {
    return BookShortDtoCache(
        id = id,
        bookId = bookId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
        originalBookName = originalBookName,
        originalAuthorName = originalAuthorName,
        description = description,
        numbersOfPages = numbersOfPages,
        isbn = isbn,
        bookGenreId = bookGenreId,
        ageRestrictions = ageRestrictions,
        imageName = imageName,
        isRussian = isRussian,
        rawCoverUrl = rawCoverUrl,
        imageFolderId = imageFolderId,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        reviewCount = reviewCount,
        ratingSum = ratingSum,
        mainBookId = mainBookId,
        isMainBook = isMainBook,
        lang = lang,
        publicationYear = publicationYear,
    )
}

fun BookShortDtoCache.toCacheShortBook(): BookShortRemoteDto {
    return BookShortRemoteDto(
        id = id,
        bookId = bookId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
        originalBookName = originalBookName,
        originalAuthorName = originalAuthorName,
        description = description,
        numbersOfPages = numbersOfPages,
        isbn = isbn,
        bookGenreId = bookGenreId,
        ageRestrictions = ageRestrictions,
        imageName = imageName,
        isRussian = isRussian,
        rawCoverUrl = rawCoverUrl,
        imageFolderId = imageFolderId,
        ratingValue = ratingValue,
        ratingCount = ratingCount,
        reviewCount = reviewCount,
        ratingSum = ratingSum,
        mainBookId = mainBookId,
        isMainBook = isMainBook,
        lang = lang,
        publicationYear = publicationYear,
    )
}