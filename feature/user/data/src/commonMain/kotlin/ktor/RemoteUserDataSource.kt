package ktor

import HttpAppClient

class RemoteUserDataSource(private val httpClient: HttpAppClient) {
    suspend fun signIn() {
        httpClient.post(
            url = "user/test",
            bodyRequest = null
        )
    }
}