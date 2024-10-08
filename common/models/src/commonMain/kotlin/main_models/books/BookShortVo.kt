package main_models.books

import kotlinx.serialization.Serializable
import main_models.BookVo
import main_models.ReadingStatus
import main_models.rating_review.ReviewAndRatingVo

@Serializable
data class BookShortVo(
    val id: Int,
    val bookId: String,
    val originalAuthorId: String,
    val bookName: String,
    val originalBookName: String,
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
    val ratingValue: Double,
    val ratingCount: Int,
    val reviewCount: Int,
    val ratingSum: Int,
    val localReadingStatus: ReadingStatus? = null,
    val localCurrentUserRating: ReviewAndRatingVo? = null,
    val mainBookId: String,
    val isMainBook: Boolean,
    val lang: String,
    val publicationYear: String?,
    val authorFirstName: String,
    val authorMiddleName: String,
    val authorLastName: String,
) {
    fun createUserBookBasedOnShortBook(readingStatus: ReadingStatus, userId: Int): BookVo =
        BookVo(
            bookId = this.bookId,
            serverId = this.id,
            localId = null,
            originalAuthorId = this.originalAuthorId,
            bookName = this.bookName,
            bookNameUppercase = this.bookName.uppercase(),
            originalAuthorName = this.originalAuthorName,
            description = this.description,
            /**we don`t save covers link. Only image imageName**/
            userCoverUrl = null,
            pageCount = this.numbersOfPages,
            isbn = this.isbn,
            readingStatus = readingStatus,
            ageRestrictions = this.ageRestrictions,
            bookGenreId = this.bookGenreId,
            startDateInString = "",
            endDateInString = "",
            startDateInMillis = 0,
            endDateInMillis = 0,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isRussian = this.isRussian,
            imageName = this.imageName,
            authorIsCreatedManually = false,
            isLoadedToServer = false,
            bookIsCreatedManually = false,
            imageFolderId = this.imageFolderId,
            ratingValue = this.ratingValue,
            ratingCount = this.ratingCount,
            reviewCount = this.reviewCount,
            ratingSum = this.ratingSum,
            isServiceDevelopmentBook = false,
            originalMainBookId = getMainBookIdByShortBook(),
            lang = this.lang,
            publicationYear = this.publicationYear,
            userId = userId,
            authorFirstName = authorFirstName,
            authorLastName = authorLastName,
            authorMiddleName = authorMiddleName,
        )

    fun getMainBookIdByShortBook(): String = if (isMainBook) bookId else mainBookId
}