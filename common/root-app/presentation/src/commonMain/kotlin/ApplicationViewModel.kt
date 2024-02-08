import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.BookItemVo
import main_models.TooltipItem
import main_models.path.PathInfoVo
import menu_bar.LeftMenuBarEvents
import models.ProjectFoldersEvents
import models.SettingsDataProvider
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform

class ApplicationViewModel(
    private val interactor: ApplicationInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val settingsDataProvider: SettingsDataProvider,
) : BaseEventScope<BaseEvent>, ApplicationScope, DrawerScope {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState
    private val booksMap: MutableMap<String, BookItemVo> = mutableMapOf()

    init {
        scope.launch {
            interactor.getAllPathInfo().collect { pathInfo ->
                if (pathInfo != null) {
                    withContext(Dispatchers.Main) {
                        _uiState.value.addPathInfo(pathInfo)
                    }

                    getAllBooks()
                }
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is ProjectFoldersEvents.SelectFolderEvent -> selectFolder(event.path)
            is ProjectFoldersEvents.CreateFolder -> createFolder(event.path, event.name)
            is ProjectFoldersEvents.SelectPathInfo -> {
                selectPathInfo(event.pathInfo)
                navigationHandler.navigateToMain()
            }

            is ProjectFoldersEvents.RenamePath -> {
                scope.launch {
                    val newPath = interactor.renamePath(
                        pathInfo = event.pathInfo,
                        newName = event.newName,
                    )
                    settingsDataProvider.updateLibraryNameInFile(
                        path = newPath,
                        oldName = event.pathInfo.libraryName,
                        newName = event.newName
                    )
                }
            }

            is ProjectFoldersEvents.RestartApp -> navigationHandler.restartWindow()
            is LeftMenuBarEvents.OnSearchClickEvent -> navigationHandler.navigateToSearch()
            is LeftMenuBarEvents.OnCreateBookClickEvent -> navigationHandler.navigateToBookCreator()
            is LeftMenuBarEvents.OnSelectAnotherVaultEvent -> navigationHandler.navigateToSelectorVault()
            is LeftMenuBarEvents.OnAuthorsClickEvent -> navigationHandler.navigateToAuthorsScreen()
            is DrawerEvents.OpenBook -> openBook(
                event.painterSelectedBookInCache,
                event.bookId
            )

            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> {
                openLeftDrawerOrClose()
            }

            is DrawerEvents.OpenRightDrawerOrCloseEvent -> openRightDrawerOrClose()
        }
    }

    override fun openBook(painter: Resource<Painter>?, bookId: String) {
        _uiState.value.apply {
            painterSelectedBookInCache.value = painter
            selectedBookId.value = bookId
            navigationHandler.navigateToBookInfo()
        }
    }

    override fun closeBookScreen() {
        _uiState.value.apply {
            fullScreenBookInfo.value = false
            navigationHandler.goBack()
        }
    }

    override fun openLeftDrawerOrClose() {
        _uiState.value.apply {
            if (!showLeftDrawerState.value) {
                showLeftDrawerState.value = true
                openLeftDrawerEvent.value.invoke()
            } else {
                showLeftDrawerState.value = false
                closeLeftDrawerEvent.value.invoke()
            }
        }
    }

    override fun openRightDrawerOrClose() {
        _uiState.value.apply {
            if (!showRightDrawerState.value) {
                showRightDrawerState.value = true
                openRightDrawerEvent.value.invoke()
            } else {
                showRightDrawerState.value = false
                closeRightDrawerEvent.value.invoke()
            }
        }
    }

    override fun setTooltip(tooltip: TooltipItem) {
        tooltipHandler.setTooltip(tooltip)
    }

    override fun checkIfNeedUpdateBookItem(oldItem: BookItemVo, newItem: BookItemVo) {
        if (oldItem.bookName != newItem.bookName) {
            _uiState.value.removeBookBooksInfoUiState(id = newItem.statusId, bookId = newItem.id)
        }
    }

    override fun changedReadingStatus(oldStatusId: String, bookId: String) {
        _uiState.value.removeBookBooksInfoUiState(id = oldStatusId, bookId = bookId)
    }

    fun isDbPathIsExist(platform: Platform): Boolean {
        val isExist = interactor.isPathIsExist(platform)
        if (isExist && interactor.isAppDbIsNotInitialized()) {
            interactor.initializeAppDatabase()
        }
        return isExist
    }

    private fun selectPathInfo(pathInfo: PathInfoVo) {
        if (!pathInfo.isSelected) {
            interactor.setPathAsSelected(pathInfo.id)
        }
    }

    private fun setFolderAsSelected(path: String, libraryName: String): Boolean {
        interactor.createDbPath(path = path, libraryName = libraryName)?.let { id ->
            interactor.setPathAsSelected(id)
            return true
        }
        return false
    }

    private fun selectFolder(path: String) {
        scope.launch {
            interactor.getPathByOs(path).let { osPath ->
                settingsDataProvider.getLibraryNameIfExist(osPath)?.let { libraryName ->
                    val isSuccess = setFolderAsSelected(
                        path = osPath,
                        libraryName = libraryName
                    )
                    if (isSuccess) {
                        navigationHandler.navigateToMain()
                    }
                }
            }
        }
    }

    private fun createFolder(path: String, name: String) {
        interactor.createFolderAndGetPath(path, name)?.let { resultPath ->
            settingsDataProvider.createAppSettingsFile(
                path = resultPath,
                libraryName = name,
                themeName = "Dark" //todo
            )
            interactor.createDbPath(
                path = resultPath,
                libraryName = name
            )
            navigationHandler.navigateToMain()
        }
    }

    private suspend fun getAllBooks() {
        interactor.getAllBooks().collect { books ->
            val unique = books.subtract(booksMap.values)
            unique.forEach { book ->
                _uiState.value.addBookToBooksInfo(book)
                booksMap[book.id] = book
            }
        }
    }
}