import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import main_models.ReadingStatus
import models.ShelfEvents
import models.ShelfUiState
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform

class ShelfViewModel(
    private val platform: Platform,
    private val repository: ShelfRepository,
    private val applicationScope: ApplicationScope,
) : BaseEventScope<BaseEvent> {
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
//                repository.getBooksByStatusId(status.id).collect {
//                    withContext(Dispatchers.Main) {
//                        _uiState.value.addBooksToShelf(shelfId = status.id, books = it)
//                    }
//                }
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is ShelfEvents.ExpandShelfEvent -> {
                uiState.value.showFullShelf(shelfIndex = event.index)
                uiState.value.bottomSheetExpandEvent.value.invoke()
            }

            is DrawerEvents.OpenBook ->{
                applicationScope.openBook(event.bookId)
            }
        }
    }

    fun searchInShelf(searchedText: String, shelfIndex: Int) {
        _uiState.value.searchInFullShelf(searchedText, shelfIndex)
    }

}





