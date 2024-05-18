package ktor

import HttpAppClient
import HttpConstants.AUTHORS_SEARCH_REQUEST
import HttpConstants.AUTHOR_BOOKS_REQUEST
import HttpConstants.BOOKS_SEARCH_REQUEST
import HttpParams
import main_models.rest.authors.AuthorsResponse
import main_models.rest.base.BaseResponse
import main_models.rest.books.BookShortResponse

class RemoteSearchDataSource(private val httpClient: HttpAppClient) {

    suspend fun getAllMatchesByAuthorName(authorName: String): BaseResponse<AuthorsResponse, String>? =
        httpClient.get(
            url = AUTHORS_SEARCH_REQUEST,
            resultClass = AuthorsResponse::class,
            errorClass = String::class,
            params = mapOf(HttpParams.AUTHOR to authorName)
        )

    suspend fun getAllMatchesByBookName(searchedText: String): BaseResponse<BookShortResponse, String>? =
        httpClient.get(
            url = BOOKS_SEARCH_REQUEST,
            resultClass = BookShortResponse::class,
            errorClass = String::class,
            params = mapOf(HttpParams.BOOK to searchedText)
        )

    suspend fun getAllBooksByAuthor(id: String): BaseResponse<BookShortResponse, String>? =
        httpClient.get(
            url = AUTHOR_BOOKS_REQUEST,
            resultClass = BookShortResponse::class,
            errorClass = String::class,
            params = mapOf(HttpParams.AUHOR_ID to id)
        )
}