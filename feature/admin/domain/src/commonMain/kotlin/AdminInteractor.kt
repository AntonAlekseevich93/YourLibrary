import main_models.books.BookShortVo
import main_models.books.LANG
import main_models.rest.DataResult
import main_models.rest.Response
import main_models.rest.admin.NonModerationBooksResponse

class AdminInteractor(
    private val repository: AdminRepository,
) {
    suspend fun getBooksForModeration(lang: LANG): Response<NonModerationBooksResponse?> =
        repository.getBooksForModeration(lang)

    suspend fun setBookAsApproved(book: BookShortVo) {
        repository.setBookAsApproved(book)
    }

    suspend fun setBookAsDiscarded(book: BookShortVo) {
        repository.setBookAsDiscarded(book)
    }

    suspend fun uploadBookImage(book: BookShortVo): BookShortVo? =
        repository.uploadBookImage(book)

    suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortVo,
        changedName: String?
    ): BookShortVo? =
        repository.setBookAsApprovedWithoutUploadImage(book, changedName)

    suspend fun clearReviewAndRatingDb() {
        repository.clearReviewAndRatingDb()
    }

    suspend fun parseSingleBook(url: String): DataResult<List<BookShortVo>, String> =
        repository.parseSingleBook(url)

    suspend fun approveParsedSingleBook(book: BookShortVo): String =
        repository.approveParsedSingleBook(book)
}