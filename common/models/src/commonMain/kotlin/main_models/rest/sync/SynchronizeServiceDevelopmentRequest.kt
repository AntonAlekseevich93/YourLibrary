package main_models.rest.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.rest.books.UserServiceDevelopmentBookRemoteDto

@Serializable
data class SynchronizeServiceDevelopmentRequest(
    @SerialName("service_development_books_device_last_timestamp") val serviceDevelopmentBooksThisDeviceTimestamp: Long,
    @SerialName("service_development_books_other_devices_last_timestamp") val serviceDevelopmentBooksOtherDevicesTimestamp: Long,
    @SerialName("service_development_user_books") val serviceDevelopmentBooks: List<UserServiceDevelopmentBookRemoteDto>,
)

@Serializable
data class SynchronizeServiceDevelopmentContentResponse(
    @SerialName("missingServiceDevelopmentBooksFromServer")
    val missingServiceDevelopmentBooksFromServer: MissingServiceDevelopmentBooksFromServer? = null,
    @SerialName("currentDeviceServiceDevelopmentBooksAddedToServer")
    val currentDeviceServiceDevelopmentBooksAddedToServer: CurrentDeviceServiceDevelopmentBooksAddedToServer? = null,
    @SerialName("currentDeviceServiceDevelopmentBooksLastTimestamp")
    val currentDeviceServiceDevelopmentBooksLastTimestamp: Long?,
)

@Serializable
data class MissingServiceDevelopmentBooksFromServer(
    @SerialName("serviceDevelopmentBooksCurrentDevice") val serviceDevelopmentBooksCurrentDevice: List<UserServiceDevelopmentBookRemoteDto>,
    @SerialName("serviceDevelopmentBooksOtherDevices") val serviceDevelopmentBooksOtherDevices: List<UserServiceDevelopmentBookRemoteDto>,
)

@Serializable
data class CurrentDeviceServiceDevelopmentBooksAddedToServer(
    @SerialName("serviceDevelopmentBooks") val serviceDevelopmentBooks: List<UserServiceDevelopmentBookRemoteDto>,
)