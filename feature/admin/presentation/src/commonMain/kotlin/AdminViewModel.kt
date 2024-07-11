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
    private val appConfig: AppConfig,
) : BaseMVIViewModel<AdminUiState, BaseEvent>(AdminUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    init {
        updateUIState(
            uiStateValue.copy(
                skipLongImageLoading = appConfig.skipLongImageLoading,
                useCustomHost = appConfig.useCustomHost,
                customUrl = uiStateValue.customUrl.copy(text = appConfig.customUrl)
            )
        )
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is AdminEvents.GetBooksForModerating -> getBooksForModeration()
            is AdminEvents.GetBooksForModeratingWithoutUploadingImages -> getBooksForModerationWithoutUploadingImages()
            is AdminEvents.ApprovedBook -> setBookAsApprove()
            is AdminEvents.DiscardBook -> setBookAsDiscarded()
            is AdminEvents.SelectBook -> selectBook(event.selectedBook)
            is AdminEvents.UploadBookCover -> uploadBookImage()
            is AdminEvents.SetBookAsApprovedWithoutUploadImage -> setBookAsApprovedWithoutUploadImage()
            is AdminEvents.ChangeSkipImageLongLoadingSettings -> {
                appConfig.changeSkipLongImageLoading(!uiStateValue.skipLongImageLoading)
                updateUIState(uiStateValue.copy(skipLongImageLoading = appConfig.skipLongImageLoading))
            }

            is AdminEvents.CustomUrlChanged -> {
                appConfig.changeCustomUrl(event.url.text)
                updateUIState(
                    uiStateValue.copy(customUrl = event.url)
                )
            }

            is AdminEvents.ChangeNeedUseCustomUrl -> {
                appConfig.changeUseCustomHost(event.needUse)
                updateUIState(uiStateValue.copy(useCustomHost = appConfig.useCustomHost))
            }
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

    private fun getBooksForModerationWithoutUploadingImages() {
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
                            canSetBookAsApprovedWithoutUploadImage = true
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

    private fun setBookAsDiscarded() {
        val currentBook = uiStateValue.moderationBookState.selectedItem?.copy()
        if (currentBook != null) {
            scope.launch {
                interactor.setBookAsDiscarded(currentBook)
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
                val bookResponse = interactor.uploadBookImage(book)
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            moderationBookState = uiStateValue.moderationBookState.copy(
                                isUploadingBookImage = false,
                                selectedItem = bookResponse ?: book
                            )
                        )
                    )
                    if (bookResponse != null) {
                        uiStateValue.moderationBookState.booksForModeration.replaceAll { if (it.id == bookResponse.id) bookResponse else it }
                    } else if (appConfig.skipLongImageLoading) {
                        selectNextBook()
                    }
                }
            }
        }
    }

    private fun setBookAsApprovedWithoutUploadImage() {
        uiStateValue.moderationBookState.selectedItem?.let { book ->
            updateUIState(
                uiStateValue.copy(
                    moderationBookState = uiStateValue.moderationBookState.copy(
                        isUploadingBookImage = true
                    )
                )
            )

            scope.launch(Dispatchers.IO) {
                val bookResponse = interactor.setBookAsApprovedWithoutUploadImage(book)
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            moderationBookState = uiStateValue.moderationBookState.copy(
                                isUploadingBookImage = false,
                                selectedItem = bookResponse ?: book
                            )
                        )
                    )
                    if (bookResponse != null) {
                        uiStateValue.moderationBookState.booksForModeration.replaceAll { if (it.id == bookResponse.id) bookResponse else it }
                    } else if (appConfig.skipLongImageLoading) {
                        selectNextBook()
                    }
                }
            }
        }
    }
}