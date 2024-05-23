package ktor

import HttpAppClient
import HttpConstants.ADD_NEW_BOOK_BY_USER
import main_models.rest.books.UserBookRemoteDto
import main_models.rest.books.UserBookResponse

class RemoteBookCreatorDataSource(private val httpClient: HttpAppClient) {
    suspend fun addNewUserBook(userBook: UserBookRemoteDto) = httpClient.post(
        url = ADD_NEW_BOOK_BY_USER,
        resultClass = UserBookResponse::class,
        errorClass = String::class,
        bodyRequest = userBook
    )
}