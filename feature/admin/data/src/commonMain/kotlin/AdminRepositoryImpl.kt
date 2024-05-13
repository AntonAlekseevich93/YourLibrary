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
    private val localAdminDataSource: LocalAdminDataSource,
    private val remoteConfig: RemoteConfig,
) : AdminRepository {

    override suspend fun getBooksForModeration(): Response<NonModerationBooksResponse?> {
        val response = remoteAdminDataSource.getBooksForModeration()
        return if (response?.result?.books != null) {
            Response.Success(
                NonModerationBooksResponse(
                    books = response.result?.books?.mapNotNull {
                        it.toVo(
                            remoteConfig.S3_IMAGE_URL_PREFIX,
                            canShowEmptyImage = true
                        )
                    },
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

    override suspend fun setBookAsDiscarded(book: BookShortVo) {
        remoteAdminDataSource.setBookAsDiscarded(book.toDto())
    }

    override suspend fun uploadBookImage(book: BookShortVo): String? {
        val imageName = remoteAdminDataSource.uploadBookImage(book = book.toDto())?.result
        return if (imageName != null) {
            remoteConfig.getImageUrl(imageName)
        } else null
    }

}