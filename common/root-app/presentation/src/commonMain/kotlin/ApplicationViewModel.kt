import base.BaseMVIViewModel
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
import models.ApplicationUiState
import models.ProjectFoldersEvents
import models.SettingsDataProvider
import navigation.RootComponent
import navigation.activeScreenAsBookInfoOrNull
import navigation.isBookInfo
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import platform.PlatformInfoData
import tooltip_area.TooltipEvents

class ApplicationViewModel(
    private val interactor: ApplicationInteractor,
    private val tooltipHandler: TooltipHandler,
    private val settingsDataProvider: SettingsDataProvider,
    private val userInteractor: UserInteractor,
    private val platformInfo: PlatformInfoData,
) : BaseMVIViewModel<ApplicationUiState, BaseEvent>(ApplicationUiState()),
    ApplicationScope, DrawerScope {

    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null

    lateinit var component: RootComponent

    init {

        uiState.value.isHazeBlurEnabled.value = platformInfo.isHazeBlurEnabled
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

            is MainAppBarEvents.OnSearch -> {
                searchInLocalBooks(event.text)
            }

            is DrawerEvents.OpenBook -> openBookInfoScreen(event.bookId, null, true)

            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> {
                openLeftDrawerOrClose()
            }

            is DrawerEvents.OpenRightDrawerOrCloseEvent -> openRightDrawerOrClose()
        }
    }

    override fun openBookInfoScreen(
        bookId: Long?,
        shortBook: BookShortVo?,
        needSaveScreenId: Boolean
    ) {
        when (val item = component.screenStack.value.active.instance) {
            is RootComponent.Screen.MainScreen -> {
                component.screenStack
                item.component.openBookInfo(bookId = bookId, needSaveScreenId = needSaveScreenId)
            }

            is RootComponent.Screen.BookInfoScreen -> {
                item.component.openBookInfo(shortVo = shortBook, bookId = bookId)
            }

            is RootComponent.Screen.BookCreatorScreen -> {
                item.component.openBookInfo(
                    bookId = bookId,
                    shortBook = shortBook,
                    needSaveScreenId = needSaveScreenId
                )
            }

            else -> {
                //nop
            }
        }
        if (component.isBookInfo()) {
            component.activeScreenAsBookInfoOrNull()?.component?.setBookShort(
                shortBook
            )
        }
    }

    override fun closeBookInfoScreen() {
        component.activeScreenAsBookInfoOrNull()?.component?.onBackClicked()
    }

    override fun openLeftDrawerOrClose() {

    }

    override fun openRightDrawerOrClose() {
    }

    override fun setTooltip(tooltip: TooltipItem) {
        tooltipHandler.setTooltip(tooltip)
    }

    override fun changedReadingStatus(oldStatusId: String, bookId: String) {
        uiStateValue.removeBookBooksInfoUiState(id = oldStatusId, bookId = bookId)
    }

    override fun navigateToBooksListInfo(books: List<BookShortVo>) {
        uiStateValue.booksListInfoScreenBooks.value = books
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
        }
    }
}