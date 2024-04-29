import androidx.compose.runtime.snapshots.SnapshotStateList
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import main_models.books.BookShortVo
import models.AdminEvents
import models.AdminUiState
import platform.Platform

class AdminViewModel(
    private val platform: Platform,
    private val interactor: AdminInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val drawerScope: DrawerScope,
) : BaseMVIViewModel<AdminUiState, BaseEvent>(AdminUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is AdminEvents.GetBooksForModerating -> getBooksForModeration()
            is AdminEvents.ApprovedBook -> setBookAsApprove()
            is AdminEvents.SelectBook -> selectBook(event.selectedBook)
        }
    }

    private fun getBooksForModeration() {
        scope.launch {
            updateUIState(uiStateValue.copy(isLoading = true))
            interactor.getBooksForModeration().data?.books?.let {
                val newList = SnapshotStateList<BookShortVo>()
                newList.addAll(it)
                val selectedPosition = 0
                updateUIState(
                    uiStateValue.copy(
                        booksForModeration = newList,
                        selectedItem = if (newList.isNotEmpty()) newList[selectedPosition] else null,
                        isLoading = false
                    )
                )
            }
        }
    }

    private fun setBookAsApprove() {
        val currentBook = uiStateValue.selectedItem?.copy()
        if (currentBook != null) {
            scope.launch {
                interactor.setBookAsApproved(currentBook)
            }
            selectNextBook()
        }
    }

    private fun selectNextBook() {
        uiStateValue.apply {
            val listSize = booksForModeration.size
            val currentBookIndex = booksForModeration.indexOf(uiStateValue.selectedItem)
            var nextBook: BookShortVo? = null
            val newList = booksForModeration //todo это равнозначно

            if (currentBookIndex == listSize - 1) {
                if (listSize > 1) {
                    nextBook = booksForModeration[0]
                    newList.remove(selectedItem)
                } else {
                    newList.clear()
                }
            } else {
                nextBook = booksForModeration.getOrNull(currentBookIndex + 1)
                newList.remove(selectedItem)
            }

            updateUIState(
                uiStateValue.copy(
                    booksForModeration = newList,
                    selectedItem = nextBook
                )
            )
        }
    }

    private fun selectBook(selectedBook: BookShortVo) {
        updateUIState(
            uiStateValue.copy(
                selectedItem = selectedBook
            )
        )
    }
}