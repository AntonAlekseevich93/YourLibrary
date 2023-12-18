package models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class SearchUiState {
    val isSearchingProcess = mutableStateOf(false)
    val isEmptySearch = mutableStateOf(false)
    val recentSearchList = mutableStateListOf<String>(
        "поиск",
        "запроса",
        "был обнаружен"
    )
    val showRecentSearchList = mutableStateOf(true)
    val searchedTasks = mutableStateListOf<SearchTaskItemUiState>()
}