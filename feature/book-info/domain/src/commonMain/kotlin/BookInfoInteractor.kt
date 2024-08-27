import kotlinx.coroutines.flow.Flow
import main_models.BookVo
import main_models.ReadingStatus
import main_models.rating_review.ReviewAndRatingVo

class BookInfoInteractor(
    private val repository: BookInfoRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository,
) {

    suspend fun getLocalBookByLocalId(localBookId: Long): Flow<BookVo?> =
        repository.getLocalBookByLocalId(localBookId)

    suspend fun getLocalBookById(bookId: String): Flow<BookVo?> =
        repository.getLocalBookById(bookId)

    suspend fun changeUserBookReadingStatus(book: BookVo, newStatus: ReadingStatus) {
        repository.updateUserBook(
            book = book.copy(readingStatus = newStatus)
        )
    }

    suspend fun getAllBooksByAuthor(authorId: String) =
        searchRepository.getAllBooksByAuthor(authorId)

    suspend fun getCurrentUserReviewAndRatingByBook(bookId: String): Flow<ReviewAndRatingVo?> =
        reviewAndRatingRepository.getCurrentUserLocalReviewAndRatingByBookFlow(bookId)

    suspend fun getAllRemoteReviewsAndRatingsByBookId(bookId: String) =
        reviewAndRatingRepository.getAllRemoteReviewsAndRatingsByBookId(bookId)

    suspend fun updateOrCreateRating(
        newRating: Int,
        bookId: String,
        bookAuthorId: String,
        bookGenreId: Int,
        isCreatedManuallyBook: Boolean,
        bookForAllUsers: Boolean,
    ) {
        reviewAndRatingRepository.addOrUpdateRatingByBookId(
            newRating = newRating,
            bookId = bookId,
            bookAuthorId = bookAuthorId,
            bookGenreId = bookGenreId,
            isCreatedManuallyBook = isCreatedManuallyBook,
            bookForAllUsers = bookForAllUsers,
        )
    }

    suspend fun addReview(reviewText: String, bookId: String) {
        reviewAndRatingRepository.addReviewByBookId(
            reviewText = reviewText,
            bookId = bookId
        )
    }

    suspend fun updateBookReadingStartDate(
        book: BookVo,
        startDateInMillis: Long,
        startDateInString: String
    ) {
        val updatedBook = book.copy(
            startDateInMillis = startDateInMillis,
            startDateInString = startDateInString
        )
        repository.updateUserBook(updatedBook)
    }

    suspend fun updateBookReadingEndDate(
        book: BookVo,
        endDateInMillis: Long,
        endDateInString: String
    ) {
        val updatedBook = book.copy(
            endDateInMillis = endDateInMillis,
            endDateInString = endDateInString
        )
        repository.updateUserBook(updatedBook)
    }

    suspend fun deleteBookReadingStartAndEndDate(
        book: BookVo,
    ) {
        val updatedBook = book.copy(
            startDateInMillis = 0,
            startDateInString = "",
            endDateInMillis = 0,
            endDateInString = ""
        )
        repository.updateUserBook(updatedBook)
    }

    suspend fun deleteBookReadingEndDate(
        book: BookVo,
    ) {
        val updatedBook = book.copy(
            endDateInMillis = 0,
            endDateInString = ""
        )
        repository.updateUserBook(updatedBook)
    }

}