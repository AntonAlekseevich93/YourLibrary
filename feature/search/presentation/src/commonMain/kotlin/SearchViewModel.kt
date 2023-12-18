import androidx.compose.ui.text.SpanStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.BookItemVo
import models.SearchTaskItemUiState
import models.SearchUiState
import utils.toHighlightText

class SearchViewModel(private val repository: SearchRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun search(searchText: String, style: SpanStyle) {
        _uiState.value.apply {
            searchedTasks.clear()
            isEmptySearch.value = false
            searchJob?.cancel()
            if (searchText.length > 2) {
                showRecentSearchList.value = false
                isSearchingProcess.value = true
                searchJob = scope.launch {
                    val result: List<BookItemVo> = repository.searchTasks(searchText)
                    if (result.isEmpty()) {
                        isEmptySearch.value = true
                    }
                    result.forEach { task ->
                        val name = task.name.toHighlightText(
                            searchText = searchText,
                            style = style
                        )
                        val description = task.description.toHighlightText(
                            searchText = searchText,
                            style = style
                        )
                        searchedTasks.add(
                            SearchTaskItemUiState(
                                id = task.id,
                                name = name,
                                description = description
                            )
                        )
                    }
                    isSearchingProcess.value = false
                }
            } else if (searchText.isEmpty()) {
                isSearchingProcess.value = false
                showRecentSearchList.value = true
            }
        }
    }

    fun clearSearch() {
        _uiState.value.apply {
            searchedTasks.clear()
            isEmptySearch.value = false
            searchJob?.cancel()
            isSearchingProcess.value = false
            showRecentSearchList.value = true
        }
    }

    fun saveRecentSearchItem(item: String) {
        // todo нужно хранить определенное кол-во items
    }

    fun deleteResentSearchItem(item: String) {

    }

}


