package ktor

import HttpAppClient
import HttpConstants.GET_ALL_USER_BOOKS
import main_models.rest.books.BooksWithAuthorsResponse

class RemoteBookInfoDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllBooksAndAuthorsByTimestamp(params: Map<String, String>) = httpClient.get(
        url = GET_ALL_USER_BOOKS,
        resultClass = BooksWithAuthorsResponse::class,
        errorClass = String::class,
        params = params
    )
}