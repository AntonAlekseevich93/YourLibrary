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

    suspend fun setBookAsDiscarded(book: BookShortVo) {
        repository.setBookAsDiscarded(book)
    }

    suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortVo,
        changedName: String?
    ): BookShortVo? =
        repository.setBookAsApprovedWithoutUploadImage(book, changedName)

    suspend fun approveAllBooksByIds(ids: List<Int>) {
        repository.approveAllBooksByIds(ids)
    }

    suspend fun clearReviewAndRatingDb() {
        repository.clearReviewAndRatingDb()
    }

    suspend fun clearAllDb() {
        repository.clearAllDb()
    }

    suspend fun parseSingleBook(url: String): DataResult<List<BookShortVo>, String> =
        repository.parseSingleBook(url)

    suspend fun approveParsedSingleBook(book: BookShortVo): String =
        repository.approveParsedSingleBook(book)

    suspend fun sendTestNotificationForCurrentUser(title: String, body: String) {
        repository.sendTestNotificationForCurrentUser(title, body)
    }
}