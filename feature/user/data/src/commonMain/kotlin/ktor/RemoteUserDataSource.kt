package ktor

import HttpAppClient
import HttpConstants.USER_SIGN_IN_REQUEST
import HttpConstants.USER_SIGN_UP_REQUEST
import HttpConstants.USER_STATUS_REQUEST
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.rest.users.UserStatus

class RemoteUserDataSource(private val httpClient: HttpAppClient) {

    suspend fun signUp(request: AuthRegisterRequest) = httpClient.post(
        url = USER_SIGN_UP_REQUEST,
        bodyRequest = request,
        resultClass = AuthResponse::class,
        errorClass = String::class
    )

    suspend fun signIn(request: AuthRequest) = httpClient.post(
        url = USER_SIGN_IN_REQUEST,
        bodyRequest = request,
        resultClass = AuthResponse::class,
        errorClass = String::class
    )

    suspend fun isAuthorized(): Boolean = httpClient.get(
        url = USER_STATUS_REQUEST,
        resultClass = UserStatus::class,
        errorClass = String::class
    )?.result?.tokenExist ?: false

}