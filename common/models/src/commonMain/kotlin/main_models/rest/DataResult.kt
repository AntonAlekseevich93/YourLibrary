package main_models.rest

sealed class DataResult<out T : Any, out E : Any> {
    data class Success<T : Any>(val value: T) : DataResult<T, Nothing>()
    data class Failure<E : Any>(val error: E) : DataResult<Nothing, E>()

    companion object {
        fun <T : Any> success(value: T): Success<T> = Success(value)
        fun <E : Any> failure(error: E): Failure<E> = Failure(error)
    }
}

val <T : Any> DataResult<T, *>.success: T?
    get() = (this as? DataResult.Success)?.value

val <E : Any> DataResult<*, E>.failure: E?
    get() = (this as? DataResult.Failure)?.error