package ktor.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksIdsRequest(
    @SerialName("ids") val booksIds: List<Int>
)