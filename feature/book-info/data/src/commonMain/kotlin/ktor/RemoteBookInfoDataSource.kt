package ktor

import HttpAppClient
import HttpConstants.GET_ALL_USER_BOOKS
import main_models.rest.SynchronizeBooksWithAuthorsRequest
import main_models.rest.SynchronizeBooksWithAuthorsResponse

class RemoteBookInfoDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllBooksAndAuthorsByTimestamp(body: SynchronizeBooksWithAuthorsRequest) =
        httpClient.post(
            url = GET_ALL_USER_BOOKS,
            resultClass = SynchronizeBooksWithAuthorsResponse::class,
            errorClass = String::class,
            bodyRequest = body
        )
}