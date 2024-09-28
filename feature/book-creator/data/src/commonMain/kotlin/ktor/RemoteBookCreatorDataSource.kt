package ktor

import HttpAppClient
import HttpConstants.ADD_NEW_BOOK_BY_USER
import HttpParams.IS_SERVICE_DEVELOPMENT
import main_models.rest.books.UserBookRemoteDto
import main_models.rest.books.UserBookResponse

class RemoteBookCreatorDataSource(private val httpClient: HttpAppClient) {
    suspend fun addNewUserBook(userBook: UserBookRemoteDto, isServiceDevelopment: Boolean) =
        httpClient.post(
            url = ADD_NEW_BOOK_BY_USER,
            resultClass = UserBookResponse::class,
            errorClass = String::class,
            bodyRequest = userBook,
            params = mapOf(IS_SERVICE_DEVELOPMENT to isServiceDevelopment.toString())
        )
}