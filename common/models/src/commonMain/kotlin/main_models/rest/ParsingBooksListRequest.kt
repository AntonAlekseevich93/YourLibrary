package main_models.rest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParsingBooksListRequest(
    @SerialName("urls") val urls: List<String>,
    @SerialName("isRussian") val isRussian: Boolean?,
)