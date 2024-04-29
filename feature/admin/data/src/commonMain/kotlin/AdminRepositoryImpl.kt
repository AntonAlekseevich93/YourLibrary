import database.LocalAdminDataSource
import ktor.RemoteAdminDataSource
import main_models.books.BookShortVo
import main_models.rest.BackendErrors
import main_models.rest.Response
import main_models.rest.admin.NonModerationBooksResponse
import main_models.rest.books.toDto
import main_models.rest.books.toVo

class AdminRepositoryImpl(
    private val remoteAdminDataSource: RemoteAdminDataSource,
    private val localAdminDataSource: LocalAdminDataSource
) : AdminRepository {

    override suspend fun getBooksForModeration(): Response<NonModerationBooksResponse?> {
        val response = remoteAdminDataSource.getBooksForModeration()
        return if (response?.result?.books != null) {
            Response.Success(
                NonModerationBooksResponse(
                    books = response.result?.books?.mapNotNull { it.toVo() },
                    error = null
                )
            )
        } else {
            Response.Error(
                error = BackendErrors.UNKNOWN,
                data = null
            )
        }
    }

    override suspend fun setBookAsApproved(book: BookShortVo) {
        remoteAdminDataSource.setBookAsApproved(book.toDto())
    }
}