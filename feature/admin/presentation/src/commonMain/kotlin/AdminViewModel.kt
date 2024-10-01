import androidx.compose.runtime.snapshots.SnapshotStateList
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.books.BookShortVo
import main_models.books.LANG
import main_models.rest.failure
import main_models.rest.success
import models.AdminEvents
import models.AdminUiState
import models.ModerationBookState
import platform.Platform
import platform.PlatformInfoData

class AdminViewModel(
    private val platform: Platform,
    private val interactor: AdminInteractor,
    private val appConfig: AppConfig,
    private val platformInfo: PlatformInfoData,
    private val applicationScope: ApplicationScope,
) : BaseMVIViewModel<AdminUiState, BaseEvent>(AdminUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

    init {
        uiState.value.isHazeBlurEnabled.value = platformInfo.isHazeBlurEnabled
        updateUIState(
            uiStateValue.copy(
                useCustomHost = appConfig.useCustomHost,
                useHttp = appConfig.useHttp,
                customUrl = uiStateValue.customUrl.copy(text = appConfig.customUrl),
                useNonModerationRange = appConfig.useNonModerationRange,
                rangeStart = uiStateValue.rangeStart.copy(text = appConfig.startNonModerationRange),
                rangeEnd = uiStateValue.rangeEnd.copy(text = appConfig.endNonModerationRange),
            )
        )
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is AdminEvents.GetRussianBooksForModeration -> getBooksForModeration(
                lang = LANG.RUSSIAN,
                coversScreen = event.coversScreen
            )

            is AdminEvents.GetEnglishBooksForModeration -> getBooksForModeration(
                lang = LANG.ENGLISH,
                coversScreen = event.coversScreen
            )

            is AdminEvents.DiscardBook -> setBookAsDiscarded()
            is AdminEvents.OnDiscardBookCovers -> setDiscardBookCover(event.book)
            is AdminEvents.SelectBook -> selectBook(event.selectedBook)
            is AdminEvents.SetBookAsApprovedWithoutUploadImage -> setBookAsApprovedWithoutUploadImage()
            is AdminEvents.ApproveAllBooks -> approveAllBooks()

            is AdminEvents.CustomUrlChanged -> {
                appConfig.changeCustomUrl(event.url.text)
                updateUIState(
                    uiStateValue.copy(customUrl = event.url)
                )
            }

            is AdminEvents.ChangeNonModerationStartRange -> {
                appConfig.changeNonModerationStartRange(event.startRange.text)
                updateUIState(uiStateValue.copy(rangeStart = event.startRange))
            }

            is AdminEvents.ChangeNonModerationEndRange -> {
                appConfig.changeNonModerationEndRange(event.endRange.text)
                updateUIState(uiStateValue.copy(rangeEnd = event.endRange))
            }

            is AdminEvents.ChangeNeedUseCustomUrl -> {
                appConfig.changeUseCustomHost(event.needUse)
                updateUIState(uiStateValue.copy(useCustomHost = appConfig.useCustomHost))
            }

            is AdminEvents.ChangeNeedUseHttp -> {
                appConfig.changeUseHttp()
                updateUIState(uiStateValue.copy(useHttp = appConfig.useHttp))
            }

            is AdminEvents.ChangeNeedUseNonModerationRange -> {
                appConfig.changeUseNonModerationRange(event.needUse)
                updateUIState(uiStateValue.copy(useNonModerationRange = appConfig.useNonModerationRange))
            }

            is AdminEvents.OnBack -> {
                if (uiStateValue.databaseMenuScreen.value) {
                    uiStateValue.databaseMenuScreen.value = false
                } else {
                    updateUIState(uiStateValue.copy(moderationBookState = ModerationBookState()))
                }
            }

            is AdminEvents.OnChangeBookName -> {
                uiStateValue.moderationBookState.showChangedBookNameField.value = true
            }

            is AdminEvents.OnCancelChangeBookName -> {
                uiStateValue.moderationBookState.showChangedBookNameField.value = false
            }

            is AdminEvents.OnDeleteChangeBookName -> {
                uiStateValue.moderationBookState.showChangedBookNameField.value = true
                uiStateValue.moderationBookState.moderationChangedName.value = null
            }

            is AdminEvents.OnSaveChangeBookName -> {
                uiStateValue.moderationBookState.showChangedBookNameField.value = false
                uiStateValue.moderationBookState.moderationChangedName.value = event.newBookName
            }

            is AdminEvents.OpenDatabaseMenuScreen -> {
                uiStateValue.databaseMenuScreen.value = true
            }

            is AdminEvents.ClearReviewAndRatingDb -> {
                scope.launch {
                    interactor.clearReviewAndRatingDb()
                }
            }

            is AdminEvents.SendTestNotification -> {
                scope.launch {
                    interactor.sendTestNotificationForCurrentUser(
                        title = event.title,
                        body = event.body
                    )
                }
            }

            is AdminEvents.ClearAllDb -> {
                scope.launch {
                    interactor.clearAllDb()
                }
            }

            is AdminEvents.OnOpenModerationScreen -> {
                applicationScope.openModerationScreen()
            }

            is AdminEvents.OnParseSingleBook -> {
                parseSingleBook(event.url)
            }

            is AdminEvents.OnApproveParsedSingleBook -> {
                approveParsedSingleBook()
            }

            is AdminEvents.OnClearParsedSingleBookData -> {
                clearParsedSingleBookData()
            }

            is AdminEvents.OnBookSelected -> {
                applicationScope.openBookInfoScreen(
                    bookId = null,
                    shortBook = event.shortBook,
                    needSaveScreenId = false
                )
            }
        }
    }

    private fun getBooksForModeration(lang: LANG, coversScreen: Boolean = false) {
        scope.launch {
            updateUIState(uiStateValue.copy(isLoading = true))
            interactor.getBooksForModeration(lang).data?.books?.let {
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
                withContext(Dispatchers.Main) {
                    if (coversScreen) {
                        applicationScope.openModerationBooksCoversScreen()
                    } else {
                        applicationScope.openModerationBooksScreen()
                    }
                }
            }
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

    private fun setDiscardBookCover(book: BookShortVo) {
        scope.launch {
            interactor.setBookAsDiscarded(book)
            withContext(Dispatchers.Main) {
                uiStateValue.moderationBookState.booksForModeration.remove(
                    book
                )
            }
        }
    }

    private fun selectNextBook() {
        uiStateValue.moderationBookState.moderationChangedName.value = null
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
                val bookResponse =
                    interactor.setBookAsApprovedWithoutUploadImage(
                        book,
                        changedName = uiStateValue.moderationBookState
                            .moderationChangedName.value.takeIf { !it.isNullOrEmpty() && it != book.bookName })
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
                        uiStateValue.moderationBookState.moderationChangedName.value = null
                        uiStateValue.moderationBookState.booksForModeration.replaceAll { if (it.id == bookResponse.id) bookResponse else it }
                    } else {
                        selectNextBook()
                    }
                }
            }
        }
    }

    private fun approveAllBooks() {
        updateUIState(uiStateValue.copy(isLoading = true))
        scope.launch(Dispatchers.IO) {
            val ids = uiStateValue.moderationBookState.booksForModeration.map { it.id }
            interactor.approveAllBooksByIds(ids)
            withContext(Dispatchers.Main) {
                updateUIState(uiStateValue.copy(isLoading = false))
                uiStateValue.moderationBookState.booksForModeration.clear()
            }
        }
    }

    private fun parseSingleBook(url: String) {
        uiStateValue.singleParsingBookProcess.value = true
        uiStateValue.singleParsingBookResultMessage.value = null
        scope.launch {
            val response = interactor.parseSingleBook(url = url)
            response.success?.firstOrNull().let { book ->
                withContext(Dispatchers.IO) {
                    uiStateValue.singleParsingBook.value = book
                }
            }
            response.failure?.let {
                uiStateValue.singleParsingBookResultMessage.value = it
            }


            uiStateValue.singleParsingBookProcess.value = false
        }
    }

    private fun approveParsedSingleBook() {
        scope.launch {
            uiStateValue.singleParsingBook.value?.let {
                uiStateValue.singleParsingBookProcess.value = true
                val serverMessage = interactor.approveParsedSingleBook(it)
                uiStateValue.singleParsingBookResultMessage.value = serverMessage
                uiStateValue.singleParsingBookProcess.value = false
            }
        }
    }

    private fun clearParsedSingleBookData() {
        uiStateValue.singleParsingBookResultMessage.value = null
        uiStateValue.singleParsingBook.value = null
    }
}