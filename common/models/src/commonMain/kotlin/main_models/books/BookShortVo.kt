package main_models.books

import kotlinx.serialization.Serializable
import main_models.ReadingStatus

@Serializable
data class BookShortVo(
    val id: Int,
    val bookId: String,
    val originalAuthorId: String,
    val bookName: String,
    val originalAuthorName: String,
    val description: String,
    val imageName: String?,
    val rawCoverUrl: String?,
    val imageResultUrl: String,
    val numbersOfPages: Int,
    val isbn: String,
    val bookGenreId: Int,
    val ageRestrictions: String?,
    val isRussian: Boolean?,
    val imageFolderId: Int?,
) {
    var readingStatus: ReadingStatus? = null
}