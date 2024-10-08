import kotlinx.coroutines.flow.Flow
import main_models.AuthorVo
import main_models.BookVo
import main_models.ReadingStatus
import main_models.rating_review.ReviewAndRatingVo

class BookInfoInteractor(
    private val repository: BookInfoRepository,
    private val bookCreatorRepository: BookCreatorRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository,
) {

    suspend fun getLocalBookByLocalId(localBookId: Long): Flow<BookVo?> =
        repository.getLocalBookByLocalId(localBookId)

    suspend fun getLocalBookById(bookId: String): Flow<BookVo?> =
        repository.getLocalBookById(bookId)

    suspend fun getLocalAuthorById(originalAuthorId: String): AuthorVo? =
        authorRepository.getLocalAuthorById(originalAuthorId)

    suspend fun changeUserBookReadingStatus(book: BookVo, newStatus: ReadingStatus) {
        repository.updateUserBook(
            book = book.copy(readingStatus = newStatus)
        )
    }

    suspend fun getAllBooksByAuthor(authorId: String) =
        searchRepository.getAllBooksByAuthor(authorId)

    suspend fun getCurrentUserReviewAndRatingByBook(mainBookId: String): Flow<ReviewAndRatingVo?> =
        reviewAndRatingRepository.getCurrentUserLocalReviewAndRatingByBookFlow(mainBookId)

    suspend fun getAllRemoteReviewsAndRatingsByBookId(mainBookId: String) =
        reviewAndRatingRepository.getAllRemoteReviewsAndRatingsByBookId(mainBookId)

    suspend fun updateOrCreateRating(
        newRating: Int,
        bookId: String,
        bookAuthorId: String,
        bookGenreId: Int,
        mainBookId: String,
    ) {
        reviewAndRatingRepository.addOrUpdateRatingByBookId(
            newRating = newRating,
            bookId = bookId,
            bookAuthorId = bookAuthorId,
            bookGenreId = bookGenreId,
            mainBookId = mainBookId
        )
    }

    suspend fun addReview(reviewText: String, mainBookId: String) {
        reviewAndRatingRepository.addReviewByBookId(
            reviewText = reviewText,
            mainBookId = mainBookId
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

    suspend fun createBook(book: BookVo, author: AuthorVo) {
        bookCreatorRepository.createBook(book, author = author)
    }

}