import main_models.books.BookShortVo
import main_models.rest.Response
import main_models.rest.admin.NonModerationBooksResponse


interface AdminRepository {
    suspend fun getBooksForModeration(): Response<NonModerationBooksResponse?>
    suspend fun setBookAsApproved(book: BookShortVo)
    suspend fun setBookAsDiscarded(book: BookShortVo)
    suspend fun uploadBookImage(book: BookShortVo): BookShortVo?
    suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortVo,
        changedName: String?
    ): BookShortVo?
}