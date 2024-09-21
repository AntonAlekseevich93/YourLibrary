package ktor

import AppConfig
import HttpAppClient
import HttpConstants.CREATE_SINGLE_APPROVED_BOOK_IN_NON_MODERATION_TABLE
import HttpConstants.GET_ALL_NON_MODERATING_BOOKS
import HttpConstants.PARSE_SINGLE_BOOK
import HttpConstants.SET_BOOK_AS_APPROVED_WITHOUT_UPLOAD_IMAGE
import HttpConstants.SET_DISCARDED_NON_MODERATING_BOOKS
import main_models.rest.ParsingBooksListRequest
import main_models.rest.books.BookShortRemoteDto
import main_models.rest.books.BookShortResponse

class RemoteAdminDataSource(
    private val httpClient: HttpAppClient,
    private val appConfig: AppConfig
) {
    suspend fun getBooksForModeration(params: Map<String, String>) = httpClient.get(
        url = GET_ALL_NON_MODERATING_BOOKS,
        resultClass = BookShortResponse::class,
        errorClass = String::class,
        params = params,
    )

    suspend fun setBookAsDiscarded(book: BookShortRemoteDto) {
        httpClient.post(
            url = SET_DISCARDED_NON_MODERATING_BOOKS,
            resultClass = String::class,
            bodyRequest = book,
            errorClass = String::class
        )
    }

    suspend fun setBookAsApprovedWithoutUploadImage(
        book: BookShortRemoteDto,
        params: Map<String, String>
    ) = httpClient.post(
        url = SET_BOOK_AS_APPROVED_WITHOUT_UPLOAD_IMAGE,
        resultClass = BookShortResponse::class,
        bodyRequest = book,
        errorClass = String::class,
        requestTimeout = 2500,
        params = params
    )

    suspend fun parseSingleBook(body: ParsingBooksListRequest) = httpClient.post(
        url = PARSE_SINGLE_BOOK,
        resultClass = BookShortResponse::class,
        bodyRequest = body,
        errorClass = String::class,
        requestTimeout = 60000
    )

    suspend fun approveParsedSingleBook(book: BookShortRemoteDto) = httpClient.post(
        url = CREATE_SINGLE_APPROVED_BOOK_IN_NON_MODERATION_TABLE,
        resultClass = String::class,
        bodyRequest = book,
        errorClass = String::class,
    )

}