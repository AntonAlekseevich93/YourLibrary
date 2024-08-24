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

}