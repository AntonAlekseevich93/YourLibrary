package ktor

import HttpAppClient
import HttpConstants.USER_INFO_REQUEST
import HttpConstants.USER_SIGN_IN_REQUEST
import HttpConstants.USER_SIGN_UP_REQUEST
import HttpConstants.USER_STATUS_REQUEST
import HttpConstants.USER_UPDATE_GOALS
import HttpParams.USER_BOOKS_GOAL
import ktor.models.UserRemoteDto
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.rest.users.UserStatusDto

class RemoteUserDataSource(private val httpClient: HttpAppClient) {

    suspend fun signUp(request: AuthRegisterRequest): BaseResponse<AuthResponse, String>? =
        httpClient.post(
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

    suspend fun getUserStatus(): UserStatusDto? = httpClient.get(
        url = USER_STATUS_REQUEST,
        resultClass = UserStatusDto::class,
        errorClass = String::class
    )?.result

    suspend fun getUser(): UserRemoteDto? = httpClient.get(
        url = USER_INFO_REQUEST,
        resultClass = UserRemoteDto::class,
        errorClass = String::class
    )?.result

    suspend fun updateUserBooksGoal(goal: Int) = httpClient.get(
        url = USER_UPDATE_GOALS,
        resultClass = UserRemoteDto::class,
        errorClass = String::class,
        params = mapOf(USER_BOOKS_GOAL to goal.toString())
    )?.result

}