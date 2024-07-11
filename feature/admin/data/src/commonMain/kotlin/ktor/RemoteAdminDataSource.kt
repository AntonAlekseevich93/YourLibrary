package ktor

import AppConfig
import HttpAppClient
import HttpConstants.GET_ALL_NON_MODERATING_BOOKS
import HttpConstants.SET_APPROVED_NON_MODERATING_BOOKS
import HttpConstants.SET_BOOK_AS_APPROVED_WITHOUT_UPLOAD_IMAGE
import HttpConstants.SET_DISCARDED_NON_MODERATING_BOOKS
import HttpConstants.UPLOAD_BOOK_IMAGE
import main_models.rest.books.BookShortRemoteDto
import main_models.rest.books.BookShortResponse

class RemoteAdminDataSource(private val httpClient: HttpAppClient, private val appConfig: AppConfig) {
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

    suspend fun setBookAsDiscarded(book: BookShortRemoteDto) {
        httpClient.post(
            url = SET_DISCARDED_NON_MODERATING_BOOKS,
            resultClass = String::class,
            bodyRequest = book,
            errorClass = String::class
        )
    }

    suspend fun uploadBookImage(book: BookShortRemoteDto) =
        httpClient.post(
            url = UPLOAD_BOOK_IMAGE,
            resultClass = BookShortResponse::class,
            bodyRequest = book,
            errorClass = String::class,
            requestTimeout = if(appConfig.skipLongImageLoading) 2500 else null
        )

    suspend fun setBookAsApprovedWithoutUploadImage(book: BookShortRemoteDto) =
        httpClient.post(
            url = SET_BOOK_AS_APPROVED_WITHOUT_UPLOAD_IMAGE,
            resultClass = BookShortResponse::class,
            bodyRequest = book,
            errorClass = String::class,
            requestTimeout = if(appConfig.skipLongImageLoading) 2500 else null
        )

}