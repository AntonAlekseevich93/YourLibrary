import androidx.compose.ui.text.SpanStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.SearchUiState

class SearchViewModel(private val repository: SearchRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun search(searchText: String, style: SpanStyle) {

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
}


