package ktor

import HttpAppClient
import HttpConstants.SYNCHRONIZE_USER_DATA
import HttpConstants.UPDATE_NOTIFICATIONS_PUSH_TOKEN
import HttpParams.NOTIFICATIONS_PUSH_TOKEN
import main_models.rest.notifications.UserNotificationRemoteDto
import main_models.rest.sync.SynchronizeUserDataContent
import main_models.rest.sync.SynchronizeUserDataRequest

class RemoteSynchronizationDataSource(private val httpClient: HttpAppClient) {
    suspend fun synchronizeUserData(body: SynchronizeUserDataRequest) =
        httpClient.post(
            url = SYNCHRONIZE_USER_DATA,
            resultClass = SynchronizeUserDataContent::class,
            errorClass = String::class,
            bodyRequest = body
        )

    suspend fun updateNotificationPushToken(pushToken: String) =
        httpClient.get(
            url = UPDATE_NOTIFICATIONS_PUSH_TOKEN,
            resultClass = UserNotificationRemoteDto::class,
            errorClass = String::class,
            params = mapOf(NOTIFICATIONS_PUSH_TOKEN to pushToken)
        )
}