package main_models.rest.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.books.BookShortVo

@Serializable
data class BookShortResponse(
    @SerialName("books") val books: List<BookShortRemoteDto>
)

@Serializable
data class BookShortRemoteDto(
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
    @SerialName("authorFirstName") val authorFirstName: String?,
    @SerialName("authorMiddleName") val authorMiddleName: String?,
    @SerialName("authorLastName") val authorLastName: String?,
)

fun BookShortRemoteDto.toVo(
    imageUrl: String?
): BookShortVo? {
    return BookShortVo(
        id = id ?: return null,
        bookId = bookId ?: return null,
        originalAuthorId = originalAuthorId ?: return null,
        bookName = bookName ?: return null,
        originalBookName = originalBookName ?: return null,
        originalAuthorName = originalAuthorName ?: return null,
        description = description ?: return null,
        imageResultUrl = imageUrl ?: return null,
        numbersOfPages = numbersOfPages ?: return null,
        isbn = isbn ?: return null,
        bookGenreId = bookGenreId ?: return null,
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
        authorFirstName = authorFirstName ?: "",
        authorLastName = authorLastName ?: "",
        authorMiddleName = authorMiddleName ?: "",
    )
}

fun BookShortVo.toDto(): BookShortRemoteDto {
    return BookShortRemoteDto(
        id = id,
        bookId = bookId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
        originalBookName = originalBookName,
        originalAuthorName = originalAuthorName,
        description = description,
        imageName = imageName,
        numbersOfPages = numbersOfPages,
        isbn = isbn,
        bookGenreId = bookGenreId,
        ageRestrictions = ageRestrictions,
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
        authorFirstName = authorFirstName,
        authorLastName = authorLastName,
        authorMiddleName = authorMiddleName,
    )
}

fun BookShortRemoteDto.toFakeVo(
): BookShortVo? {
    return BookShortVo(
        id = -1,
        bookId = bookId ?: return null,
        originalAuthorId = "",
        bookName = bookName ?: return null,
        originalBookName = originalBookName ?: return null,
        originalAuthorName = originalAuthorName ?: return null,
        description = description ?: return null,
        imageResultUrl = rawCoverUrl.orEmpty(),
        numbersOfPages = numbersOfPages ?: return null,
        isbn = isbn ?: return null,
        bookGenreId = bookGenreId ?: return null,
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
        authorFirstName = authorFirstName ?: "",
        authorLastName = authorLastName ?: "",
        authorMiddleName = authorMiddleName ?: "",
    )
}

fun BookShortVo.fromFakeToDto(
): BookShortRemoteDto {
    return BookShortRemoteDto(
        id = null,
        bookId = bookId,
        originalAuthorId = null,
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
        authorFirstName = authorFirstName,
        authorLastName = authorLastName,
        authorMiddleName = authorMiddleName,
    )
}
