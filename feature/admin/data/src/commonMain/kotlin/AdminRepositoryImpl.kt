import HttpParams.CHANGED_BOOK_NAME
import HttpParams.RANGE_END
import HttpParams.RANGE_START
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
    private val appConfig: AppConfig,
) : AdminRepository {

    override suspend fun getBooksForModeration(): Response<NonModerationBooksResponse?> {
        val params = mutableMapOf<String, String>()
        if (appConfig.useNonModerationRange) {
            appConfig.getNonModerationRangeOrNull()?.let {
                params[RANGE_START] = it.first.toString()
                params[RANGE_END] = it.last.toString()
            }
        }
        val response = remoteAdminDataSource.getBooksForModeration(params)
        return if (response?.result?.books != null) {
            Response.Success(
                NonModerationBooksResponse(
                    books = response.result?.books?.mapNotNull {
                        it.toVo(
                            imageUrl = if (it.imageName.isNullOrEmpty()) "" else remoteConfig.getImageUrl(
                                imageName = it.imageName,
                                imageFolderId = it.imageFolderId,
                                bookServerId = it.id
                            ),
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

    override suspend fun uploadBookImage(book: BookShortVo): BookShortVo? {
        val bookResult =
            remoteAdminDataSource.uploadBookImage(book = book.toDto())?.result?.books?.firstOrNull()
        return if (bookResult?.imageName != null) {
            val imageUrl = remoteConfig.getImageUrl(
                bookResult.imageName,
                imageFolderId = bookResult.imageFolderId,
                bookServerId = book.id
            )
            bookResult.toVo(imageUrl = imageUrl)
        } else null
    }

    override suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortVo,
        changedName: String?
    ): BookShortVo? {
        val params = mutableMapOf<String, String>()
        changedName?.let {
            params[CHANGED_BOOK_NAME] = it
        }
        val bookResult =
            remoteAdminDataSource.setBookAsApprovedWithoutUploadImage(
                book = book.toDto(),
                params = params
            )?.result?.books?.firstOrNull()
        return if (bookResult?.imageName != null) {
            val imageUrl = remoteConfig.getImageUrl(
                bookResult.imageName,
                imageFolderId = bookResult.imageFolderId,
                bookServerId = book.id
            )
            bookResult.toVo(imageUrl = imageUrl)
        } else null
    }

}