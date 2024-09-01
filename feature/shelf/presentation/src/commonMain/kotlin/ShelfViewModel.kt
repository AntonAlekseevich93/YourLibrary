import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.BookVo
import main_models.ReadingStatus
import models.ShelfBoardsEvents
import models.ShelfEvents
import models.ShelfUiState
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import platform.PlatformInfoData

class ShelfViewModel(
    private val platform: Platform,
    private val repository: ShelfRepository,
    private val applicationScope: ApplicationScope,
    private val platformInfo: PlatformInfoData,
) : BaseMVIViewModel<ShelfUiState, BaseEvent>(ShelfUiState(platform = platform)) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val lock = Any()

    init {
        uiState.value.isHazeBlurEnabled.value = platformInfo.isHazeBlurEnabled
        ReadingStatus.entries.forEach { status ->
            scope.launch(Dispatchers.IO) {
                repository.getAllBooksByReadingStatus(status.id).collect { books ->
                    synchronized(lock) {
                        addBooksToShelf(
                            shelfId = status.id,
                            books = books.sortedByDescending { book -> book.timestampOfCreating }
                        )
                    }
                }
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is ShelfEvents.ExpandShelfEvent -> {
                uiState.value.showFullShelf(shelfIndex = event.index)
                uiState.value.bottomSheetExpandEvent.invoke()
            }

            is DrawerEvents.OpenBook -> {
                applicationScope.openBookInfoScreen(bookId = event.bookId, shortBook = null)
            }

            is ShelfBoardsEvents.SetBottomSheetExpandListener -> {
                updateUIState(uiStateValue.copy(bottomSheetExpandEvent = event.listener))
            }

            is ShelfBoardsEvents.OnDataRefresh -> {
                updateUIState(uiStateValue.copy(isRefreshingState = true))
                scope.launch(Dispatchers.IO) {
                    repository.synchronizeBooksWithAuthors()
                    withContext(Dispatchers.Main) {
                        updateUIState(uiStateValue.copy(isRefreshingState = false))
                    }
                }

            }
        }
    }

    fun searchInShelf(searchedText: String, shelfIndex: Int) {
//        _uiState.value.searchInFullShelf(searchedText, shelfIndex)
    }

    private fun addBooksToShelf(shelfId: String, books: List<BookVo>) {
        val shelvesList = uiStateValue.shelvesList.map { shelfVo ->
            if (shelfVo.id == shelfId) {
                shelfVo.copy(booksList = books)
            } else {
                shelfVo
            }
        }
        updateUIState(uiStateValue.copy(shelvesList = shelvesList))

    }

}





