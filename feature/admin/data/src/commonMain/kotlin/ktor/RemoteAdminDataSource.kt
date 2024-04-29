package ktor

import HttpAppClient
import HttpConstants.GET_ALL_NON_MODERATING_BOOKS
import HttpConstants.SET_APPROVED_NON_MODERATING_BOOKS
import main_models.rest.books.BookShortRemoteDto
import main_models.rest.books.BookShortResponse

class RemoteAdminDataSource(private val httpClient: HttpAppClient) {
    suspend fun getBooksForModeration() = httpClient.get(
        url = GET_ALL_NON_MODERATING_BOOKS,
        resultClass = BookShortResponse::class,
        errorClass = String::class
    )

    suspend fun setBookAsApproved(book: BookShortRemoteDto) {
        httpClient.post(
            url = SET_APPROVED_NON_MODERATING_BOOKS,
            resultClass = String::class,
            bodyRequest = book,
            errorClass = String::class
        )
    }

}