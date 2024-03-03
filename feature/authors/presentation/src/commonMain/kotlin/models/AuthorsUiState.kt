package models

import alert_dialog.CommonAlertDialogConfig
import androidx.compose.material3.SnackbarHostState
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
    ),
    val authorAlertDialogState: MutableState<AuthorAlertDialogState> = mutableStateOf(
        AuthorAlertDialogState()
    ),
    val snackbarHostState: MutableState<SnackbarHostState> = mutableStateOf(SnackbarHostState())
) : BaseUIState {

    fun clearJoiningAuthors() {
        joiningAuthorsUiState.value = JoiningAuthorsUiState()
    }
}

data class JoiningAuthorsUiState(
    val mainAuthor: MutableState<AuthorVo> = mutableStateOf(AuthorVo.getEmptyAuthor()),
    val allAuthorsExceptMainAndRelates: LinkedHashMap<String, MutableList<AuthorVo>> = LinkedHashMap()
)

data class AuthorAlertDialogState(
    val show: Boolean = false,
    val selectedAuthor: AuthorVo = AuthorVo.getEmptyAuthor(),
    val config: CommonAlertDialogConfig = CommonAlertDialogConfig(
        title = "",
        acceptButtonTitle = "",
        dismissButtonTitle = ""
    ),
)