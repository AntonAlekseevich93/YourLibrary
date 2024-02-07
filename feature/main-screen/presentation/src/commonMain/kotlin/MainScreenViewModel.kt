import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.BookItemVo
import main_models.ViewsType
import menu_bar.LeftMenuBarEvents
import models.MainScreenUiState
import navigation_drawer.contents.models.DrawerEvents
import tooltip_area.TooltipEvents

class MainScreenViewModel(
    private val repository: MainScreenRepository,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val drawerScope: DrawerScope,
    private val applicationScope: ApplicationScope,
) : MainScreenScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()
    private val booksMap: MutableMap<String, BookItemVo> = mutableMapOf()

    override fun checkIfNeedUpdateBookItem(oldItem: BookItemVo, newItem: BookItemVo) {
        if (oldItem.bookName != newItem.bookName) {
            _uiState.value.removeBookBooksInfoUiState(id = newItem.statusId, bookId = newItem.id)
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> {
                drawerScope.openLeftDrawerOrClose()
            }

            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is LeftMenuBarEvents.OnSearchClickEvent -> navigationHandler.navigateToSearch()
            is LeftMenuBarEvents.OnCreateBookClickEvent -> navigationHandler.navigateToBookCreator()
            is LeftMenuBarEvents.OnSelectAnotherVaultEvent -> navigationHandler.navigateToSelectorVault()
            is LeftMenuBarEvents.OnAuthorsClickEvent -> navigationHandler.navigateToAuthorsScreen()
            is DrawerEvents.OpenBook -> applicationScope.openBook(
                event.painterSelectedBookInCache,
                event.bookId
            )
        }
    }

    override fun changedReadingStatus(oldStatusId: String, bookId: String) {
        _uiState.value.removeBookBooksInfoUiState(id = oldStatusId, bookId = bookId)
    }

    fun getSelectedPathInfo() {
        scope.launch {
            launch {
                repository.getSelectedPathInfo().collect { pathInfo ->
                    pathInfo?.let {
                        withContext(Dispatchers.Main) {
                            _uiState.value.selectedPathInfo.value = it
                        }
                    }
                }
            }
            launch {
                getAllBooks()
            }
        }
    }

    fun switchViewTypesListener(isChecked: Boolean, viewsType: ViewsType) {
        _uiState.value.viewsTypes.changeViewTypes(isChecked, viewsType)
    }

    fun changeViewsTypes() {
        _uiState.value.viewsTypes.applyCheckedViewTypes()
        //todo здесь изменить экран который открыт если этого item больше нет
    }

    fun openViewType(viewsType: ViewsType) {
        _uiState.value.viewsTypes.openedViewType.value = viewsType
    }

    private suspend fun getAllBooks() {
        repository.getAllBooks().collect { books ->
            val unique = books.subtract(booksMap.values)
            unique.forEach { book ->
                _uiState.value.addBookToBooksInfo(book)
                booksMap[book.id] = book
            }
        }
    }

}