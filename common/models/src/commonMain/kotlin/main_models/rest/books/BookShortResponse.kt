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
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("imageName") val imageName: String? = null,
    @SerialName("numbersOfPages") val numbersOfPages: Int? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("bookGenreId") val bookGenreId: Int? = null,
    @SerialName("bookGenre") val bookGenreName: String? = null,
)

fun BookShortRemoteDto.toVo(
    imagePrefixUrl: String?,
    canShowEmptyImage: Boolean = false
): BookShortVo? {
    val imageUrl =
        if (imagePrefixUrl == null) imageName else if (!imageName.isNullOrEmpty()) imagePrefixUrl + imageName else if (canShowEmptyImage) "" else null
    return BookShortVo(
        id = id ?: return null,
        bookId = bookId ?: return null,
        originalAuthorId = originalAuthorId ?: return null,
        bookName = bookName ?: return null,
        originalAuthorName = originalAuthorName ?: return null,
        description = description ?: return null,
        coverUrl = coverUrl,
        imageResultUrl = imageUrl ?: return null,
        numbersOfPages = numbersOfPages ?: return null,
        isbn = isbn ?: return null,
        bookGenreId = bookGenreId ?: return null,
        bookGenreName = bookGenreName ?: return null
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
        coverUrl = coverUrl,
        imageName = imageResultUrl,
        numbersOfPages = numbersOfPages,
        isbn = isbn,
        bookGenreId = bookGenreId,
        bookGenreName = bookGenreName
    )
}
