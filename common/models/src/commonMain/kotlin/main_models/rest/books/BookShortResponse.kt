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
    @SerialName("numbersOfPages") val numbersOfPages: Int? = null,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("bookGenreId") val bookGenreId: Int? = null,
    @SerialName("bookGenre") val bookGenreName: String? = null,
)

fun BookShortRemoteDto.toVo(): BookShortVo? {
    return BookShortVo(
        id = id ?: return null,
        bookId = bookId ?: return null,
        originalAuthorId = originalAuthorId ?: return null,
        bookName = bookName ?: return null,
        originalAuthorName = originalAuthorName ?: return null,
        description = description ?: return null,
        coverUrl = coverUrl ?: return null,
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
        numbersOfPages = numbersOfPages,
        isbn = isbn,
        bookGenreId = bookGenreId,
        bookGenreName = bookGenreName
    )
}
