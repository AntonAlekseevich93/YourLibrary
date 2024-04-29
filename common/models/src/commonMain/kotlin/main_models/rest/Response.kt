package main_models.rest

sealed class Response<T>(
    val error: BackendErrors?,
    val data: T
) {
    class Success<T>(data: T) : Response<T>(data = data, error = null)
    class Error<T>(
        error: BackendErrors,
        data: T
    ) : Response<T>(error, data)
}