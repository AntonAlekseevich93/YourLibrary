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
)

fun BookShortRemoteDto.toVo(
    imageUrl: String?
): BookShortVo? {
    return BookShortVo(
        id = id ?: return null,
        bookId = bookId ?: return null,
        originalAuthorId = originalAuthorId ?: return null,
        bookName = bookName ?: return null,
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
    )
}

fun BookShortVo.toDto(): BookShortRemoteDto {
    return BookShortRemoteDto(
        id = id,
        bookId = bookId,
        originalAuthorId = originalAuthorId,
        bookName = bookName,
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
    )
}
