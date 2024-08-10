package ktor

import HttpAppClient
import HttpConstants.SYNCHRONIZE_USER_DATA
import HttpConstants.UPDATE_USER_BOOK
import main_models.rest.books.UserBookRemoteDto
import main_models.rest.books.UserBookResponse
import main_models.rest.sync.SynchronizeUserDataContent
import main_models.rest.sync.SynchronizeUserDataRequest

class RemoteBookInfoDataSource(private val httpClient: HttpAppClient) {
    suspend fun getAllBooksAndAuthorsByTimestamp(body: SynchronizeUserDataRequest) =
        httpClient.post(
            url = SYNCHRONIZE_USER_DATA,
            resultClass = SynchronizeUserDataContent::class,
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