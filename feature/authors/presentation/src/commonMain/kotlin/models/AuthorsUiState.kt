package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState
import main_models.AuthorVo

data class AuthorsUiState(
    val authorByAlphabet: LinkedHashMap<String, MutableList<AuthorVo>> = LinkedHashMap(),
    val joiningAuthorsUiState: MutableState<JoiningAuthorsUiState> = mutableStateOf(
        JoiningAuthorsUiState()
    ),
    val searchingAuthorResult: MutableState<LinkedHashMap<String, MutableList<AuthorVo>>> = mutableStateOf(
        LinkedHashMap()
    )
) : BaseUIState {

    fun clearJoiningAuthors() {
        joiningAuthorsUiState.value = JoiningAuthorsUiState()
    }
}

data class JoiningAuthorsUiState(
    val mainAuthor: MutableState<AuthorVo> = mutableStateOf(AuthorVo.getEmptyAuthor()),
    val allAuthorsExceptMainAndRelates: LinkedHashMap<String, MutableList<AuthorVo>> = LinkedHashMap()
)