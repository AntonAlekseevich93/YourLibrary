package ktor

import HttpAppClient
import HttpConstants.SYNCHRONIZE_USER_DATA
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
}