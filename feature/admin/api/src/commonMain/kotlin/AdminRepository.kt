import main_models.books.BookShortVo
import main_models.books.LANG
import main_models.rest.DataResult
import main_models.rest.Response
import main_models.rest.admin.NonModerationBooksResponse


interface AdminRepository {
    suspend fun getBooksForModeration(lang: LANG): Response<NonModerationBooksResponse?>
    suspend fun setBookAsDiscarded(book: BookShortVo)
    suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortVo,
        changedName: String?
    ): BookShortVo?

    suspend fun clearReviewAndRatingDb()
    suspend fun clearAllDb()
    suspend fun parseSingleBook(url: String): DataResult<List<BookShortVo>, String>
    suspend fun approveParsedSingleBook(book: BookShortVo): String
    suspend fun sendTestNotificationForCurrentUser(title: String, body: String)
}