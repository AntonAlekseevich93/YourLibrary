import kotlinx.coroutines.flow.Flow
import main_models.BookVo
import main_models.ReadingStatus
import main_models.rating_review.ReviewAndRatingVo

class BooksListInfoInteractor(
    private val repository: BooksListInfoRepository,
    private val authorRepository: AuthorsRepository,
    private val searchRepository: SearchRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository,
) {

    suspend fun getLocalBookByLocalId(localBookId: Long): Flow<BookVo?> =
        repository.getLocalBookByLocalId(localBookId)

    suspend fun getLocalBookById(bookId: String): Flow<BookVo?> =
        repository.getLocalBookById(bookId)

    suspend fun getCurrentUserReviewAndRatingByBook(bookId: String): ReviewAndRatingVo? =
        reviewAndRatingRepository.getCurrentUserLocalReviewAndRatingByBook(bookId)

    suspend fun getBookReadingStatus(bookId: String): ReadingStatus? =
        repository.getBookReadingStatus(bookId)
}