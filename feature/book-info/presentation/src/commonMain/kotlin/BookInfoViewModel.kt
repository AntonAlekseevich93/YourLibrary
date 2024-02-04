import androidx.compose.ui.text.input.TextFieldValue
import book_editor.BookEditorEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import main_models.BookItemVo
import main_models.ReadingStatus
import menu_bar.LeftMenuBarEvents
import models.BookInfoScope
import models.BookInfoUiState
import models.BookScreenEvents
import navigation_drawer.contents.models.DrawerEvents
import tooltip_area.TooltipEvents

class BookInfoViewModel(
    private val platformInfo: PlatformInfo,
    private val interactor: BookInfoInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val applicationScope: ApplicationScope,
    private val drawerScope: DrawerScope,
    private val mainScreenScope: MainScreenScope<BaseEvent>
) : BookInfoScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        scope.launch {
            interactor.getSelectedPathInfo().collect { pathInfo ->
                pathInfo?.let {
                    withContext(Dispatchers.Main) {
                        _uiState.value.selectedPathInfo.value = it
                    }
                }
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is BookScreenEvents.BookScreenCloseEvent -> applicationScope.closeBookScreen()
            is BookScreenEvents.SaveBookAfterEditing -> saveBookAfterEditing()
            is BookScreenEvents.SetEditMode -> _uiState.value.isEditMode.value = true
            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged
            )

            is LeftMenuBarEvents.OnSearchClickEvent -> navigationHandler.navigateToSearch()
            is LeftMenuBarEvents.OnCreateBookClickEvent -> navigationHandler.navigateToBookCreator(
                popUpToMain = true
            )

            is LeftMenuBarEvents.OnSelectAnotherVaultEvent -> navigationHandler.navigateToSelectorVault(
                needPopBackStack = true
            )
        }
    }

    fun getBookItem(id: String) {
        scope.launch {
            interactor.getBookById(bookId = id).collect { book ->
                withContext(Dispatchers.Main) {
                    _uiState.value.setBookItem(book)
                }
                launch {
                    interactor.getAuthorWithRelatesWithoutBooks(book.authorId)?.let {
                        _uiState.value.setSelectedAuthor(it)
                        _uiState.value.authorWasSelectedProgrammatically.value.invoke()
                    }
                }
            }
        }
    }

    fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }


    fun changeReadingStatus(status: ReadingStatus, bookId: String) {
        scope.launch {
            interactor.changeBookStatusId(status, bookId)
        }
    }


    fun setSelectedAuthor(author: AuthorVo) {
        _uiState.value.setSelectedAuthor(author)
    }

    private fun updateBook(bookItem: BookItemVo, needCreateNewAuthor: Boolean) {
        scope.launch {
            if (needCreateNewAuthor) {
                val author = createNewAuthor(authorName = bookItem.authorName)
                interactor.createAuthor(author)
                interactor.updateBook(bookItem.copy(authorId = author.id))
            } else {
                interactor.updateBook(bookItem)
            }
        }
    }

    private fun searchAuthor(authorName: String) {
        searchJob?.cancel()
        if (authorName.length >= 2) {
            searchJob = scope.launch {
                val result = interactor.searchInAuthorsNameWithRelates(authorName)
                if (result.isNotEmpty()) {
                    _uiState.value.addSimilarAuthors(result)
                } else {
                    clearSearchAuthor()
                }
            }
        } else {
            clearSearchAuthor()
        }
    }

    private fun createNewAuthor(
        authorName: String
    ): AuthorVo {
        _uiState.value.apply {
            return AuthorVo(
                id = AuthorVo.generateId(),
                name = authorName,
                isMainAuthor = true,
                timestampOfCreating = platformInfo.getCurrentTime().timeInMillis,
                timestampOfUpdating = platformInfo.getCurrentTime().timeInMillis,
                relatedToAuthorId = null,
                books = emptyList()
            )
        }
    }

    private fun onAuthorTextChanged(textFieldValue: TextFieldValue, textWasChanged: Boolean) {
        _uiState.value.apply {
            if (selectedAuthor.value != null && bookValues.value.isSelectedAuthorNameWasChanged()) {
                clearSelectedAuthor()
            }
            if (textFieldValue.text.isEmpty()) {
                clearSearchAuthor()
            } else if (textWasChanged) {
                searchAuthor(textFieldValue.text)
            }
        }
    }

    private fun saveBookAfterEditing() {
        uiState.value.apply {
            if (isEditMode.value && bookItem.value != null) {
                bookValues.value.updateBookWithEmptyAuthorId(
                    bookId = bookItem.value!!.id,
                    timestampOfCreating = bookItem.value!!.timestampOfCreating,
                    timestampOfUpdating = getCurrentTimeInMillis(),
                )?.let {
                    mainScreenScope.checkIfNeedUpdateBookItem(bookItem.value!!, it)
                    updateBook(
                        bookItem = it,
                        needCreateNewAuthor = needCreateNewAuthor.value
                    )
                }
            }

            isEditMode.value = !isEditMode.value
        }
    }

    private fun getCurrentTimeInMillis(): Long = platformInfo.getCurrentTime().timeInMillis
}