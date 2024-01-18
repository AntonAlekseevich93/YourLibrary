import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.ReadingStatus
import models.ShelfUiState
import platform.Platform

class ShelfViewModel(
    private val platform: Platform,
    private val repository: ShelfRepository
) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState = MutableStateFlow(
        ShelfUiState(
            platform = platform,
            shelvesList = mutableStateOf(mutableListOf())
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        ReadingStatus.entries.forEach { status ->
            scope.launch {
                repository.getBooksByStatusId(status.id).collect {
                    withContext(Dispatchers.Main) {
                        _uiState.value.addBooksToShelf(shelfId = status.id, books = it)
                    }
                }
            }
        }
    }

    fun searchInShelf(searchedText: String, shelfIndex: Int) {
        _uiState.value.searchInFullShelf(searchedText, shelfIndex)
    }

    fun showFullShelf(shelfIndex: Int) {
        _uiState.value.showFullShelf(shelfIndex = shelfIndex)
    }

}





