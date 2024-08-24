import base.BaseMVIViewModel
import di.ViewModelStackStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_app_bar.MainAppBarEvents
import main_models.TooltipItem
import main_models.books.BookShortVo
import main_models.path.PathInfoVo
import menu_bar.LeftMenuBarEvents
import models.ApplicationUiState
import models.ProjectFoldersEvents
import models.SettingsDataProvider
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import tooltip_area.TooltipEvents

class ApplicationViewModel(
    private val interactor: ApplicationInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val settingsDataProvider: SettingsDataProvider,
    private val userInteractor: UserInteractor
) : BaseMVIViewModel<ApplicationUiState, BaseEvent>(ApplicationUiState()),
    ApplicationScope, DrawerScope {

    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null

    init {
        scope.launch {
            launch(Dispatchers.IO) {
                interactor.synchronizeBooksWithAuthors()
            }

            launch {
                interactor.getAllPathInfo().collect { pathInfo ->
                    if (pathInfo != null) {
                        withContext(Dispatchers.Main) {
                            uiStateValue.addPathInfo(pathInfo)
                        }
                    }
                }
            }

            launch {
                userInteractor.checkIfUserTokenExistAndLogOutIfNot()
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
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
            is LeftMenuBarEvents.OnSettingsClickEvent -> navigationHandler.navigateToSettingsScreen()
            is LeftMenuBarEvents.OnProfileClickEvent -> navigationHandler.navigateToProfile()
            is LeftMenuBarEvents.OnAdminPanelClickEvent -> navigationHandler.navigateToAdminPanel()
            is LeftMenuBarEvents.OnHomeClickEvent -> navigationHandler.navigateToMain()
            is MainAppBarEvents.OnSearch -> {
                searchInLocalBooks(event.text)
            }

            is DrawerEvents.OpenBook -> openBookInfoScreen(event.bookId, null)

            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> {
                openLeftDrawerOrClose()
            }

            is DrawerEvents.OpenRightDrawerOrCloseEvent -> openRightDrawerOrClose()
            is DrawerEvents.ToMain -> navigationHandler.navigateToMain()
        }
    }

    override fun openBookInfoScreen(bookId: Long?, shortBook: BookShortVo?) {
        uiStateValue.apply {
            previousBookInfoViewModel.value = null
            selectedBookId.value = bookId
            selectedShortBook.value = shortBook
            navigationHandler.navigateToBookInfo()
        }
    }

    override fun closeBookInfoScreen() {
        uiStateValue.apply {
            previousBookInfoViewModel.value = null
            ViewModelStackStore.clearViewModelStack()
            fullScreenBookInfo.value = false
            navigationHandler.goBack()
        }
    }

    override fun onBackFromBookScreen() {
        ViewModelStackStore.getPreviousViewModelOrNull<BookInfoViewModel>()?.let {
            uiStateValue.previousBookInfoViewModel.value = it
        }
        navigationHandler.goBack()
    }

    override fun openLeftDrawerOrClose() {
        uiStateValue.apply {
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
        uiStateValue.apply {
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

    override fun changedReadingStatus(oldStatusId: String, bookId: String) {
        uiStateValue.removeBookBooksInfoUiState(id = oldStatusId, bookId = bookId)
    }

    fun isDbPathIsExist(platform: Platform): Boolean {
        val isExist = interactor.isPathIsExist(platform)
        if (isExist && interactor.isAppDbIsNotInitialized()) {
            interactor.initializeAppDatabase()
        }
        return isExist
    }

    private fun searchInLocalBooks(searchedText: String) {
        searchJob?.cancel()
        searchJob = scope.launch(Dispatchers.IO) {
            if (searchedText.length >= 2) {
                val books = interactor.searchInLocalBooks(searchedText)
                withContext(Dispatchers.Main) {
                    updateUIState(uiStateValue.copy(searchedBooks = books))
                }
            } else if (uiStateValue.searchedBooks.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    updateUIState(uiStateValue.copy(searchedBooks = emptyList()))
                }
            }
        }
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
}