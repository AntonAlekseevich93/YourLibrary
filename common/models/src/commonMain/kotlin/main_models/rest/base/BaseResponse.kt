package main_models.rest.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class BaseResponse<out TResult, out TError>(
    @SerialName("data") val result: TResult? = null,
    @SerialName("errorMessage") val error: TError? = null
)