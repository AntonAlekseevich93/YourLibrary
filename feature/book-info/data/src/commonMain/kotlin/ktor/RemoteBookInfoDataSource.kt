package ktor

import HttpAppClient
import HttpConstants.GET_ALL_USER_BOOKS
import HttpConstants.UPDATE_USER_BOOK
import main_models.rest.SynchronizeBooksWithAuthorsRequest
import main_models.rest.SynchronizeBooksWithAuthorsResponse
import main_models.rest.books.UserBookRemoteDto
import main_models.rest.books.UserBookResponse

class RemoteBookInfoDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllBooksAndAuthorsByTimestamp(body: SynchronizeBooksWithAuthorsRequest) =
        httpClient.post(
            url = GET_ALL_USER_BOOKS,
            resultClass = SynchronizeBooksWithAuthorsResponse::class,
            errorClass = String::class,
            bodyRequest = body
        )

    suspend fun updateUserBook(userBook: UserBookRemoteDto) = httpClient.post(
        url = UPDATE_USER_BOOK,
        resultClass = UserBookResponse::class,
        errorClass = String::class,
        bodyRequest = userBook
    )
}