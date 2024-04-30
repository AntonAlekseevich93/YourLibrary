import androidx.compose.runtime.snapshots.SnapshotStateList
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.books.BookShortVo
import models.AdminEvents
import models.AdminUiState
import models.ModerationBookState
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
            is AdminEvents.UploadBookCover -> uploadBookImage()
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
                        moderationBookState = ModerationBookState(
                            booksForModeration = newList,
                            selectedItem = if (newList.isNotEmpty()) newList[selectedPosition] else null,
                        ),
                        isLoading = false
                    )
                )
            }
        }
    }

    private fun setBookAsApprove() {
        val currentBook = uiStateValue.moderationBookState.selectedItem?.copy()
        if (currentBook != null) {
            scope.launch {
                interactor.setBookAsApproved(currentBook)
            }
            selectNextBook()
        }
    }

    private fun selectNextBook() {
        uiStateValue.moderationBookState.apply {
            val listSize = booksForModeration.size
            val currentBookIndex = booksForModeration.indexOf(selectedItem)
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
                    moderationBookState = uiStateValue.moderationBookState.copy(
                        booksForModeration = newList,
                        selectedItem = nextBook
                    )
                )
            )
        }
    }

    private fun selectBook(selectedBook: BookShortVo) {
        updateUIState(
            uiStateValue.copy(
                moderationBookState = uiStateValue.moderationBookState.copy(
                    selectedItem = selectedBook
                )
            )
        )
    }

    private fun uploadBookImage() {
        uiStateValue.moderationBookState.selectedItem?.let { book ->
            updateUIState(
                uiStateValue.copy(
                    moderationBookState = uiStateValue.moderationBookState.copy(
                        isUploadingBookImage = true
                    )
                )
            )

            scope.launch(Dispatchers.IO) {
                val url = interactor.uploadBookImage(book)
                val resultBook = book.copy(
                    imageResultUrl = url.orEmpty()
                )
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            moderationBookState = uiStateValue.moderationBookState.copy(
                                isUploadingBookImage = false,
                                selectedItem = resultBook
                            )
                        )
                    )
                    uiStateValue.moderationBookState.booksForModeration.replaceAll { if (it.id == resultBook.id) resultBook else it }
                }
            }
        }
    }
}