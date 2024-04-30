package main_models.books

import kotlinx.serialization.Serializable

@Serializable
data class BookShortVo(
    val id: Int,
    val bookId: String,
    val originalAuthorId: String,
    val bookName: String,
    val originalAuthorName: String,
    val description: String,
    val coverUrl: String?,
    val imageResultUrl: String,
    val numbersOfPages: Int,
    val isbn: String,
    val bookGenreId: Int,
    val bookGenreName: String
)