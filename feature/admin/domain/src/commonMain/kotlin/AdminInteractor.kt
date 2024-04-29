import main_models.books.BookShortVo
import main_models.rest.Response
import main_models.rest.admin.NonModerationBooksResponse

class AdminInteractor(
    private val repository: AdminRepository,
) {
    suspend fun getBooksForModeration(): Response<NonModerationBooksResponse?> =
        repository.getBooksForModeration()

    suspend fun setBookAsApproved(book: BookShortVo) {
        repository.setBookAsApproved(book)
    }
}