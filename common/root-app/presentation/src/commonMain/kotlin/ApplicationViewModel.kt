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
import models.ApplicationUiState
import navigation.RootComponent
import navigation.activeScreenAsBookInfoOrNull
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import platform.PlatformInfoData
import tooltip_area.TooltipEvents

class ApplicationViewModel(
    private val interactor: ApplicationInteractor,
    private val tooltipHandler: TooltipHandler,
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
                item.component.openBookInfo(
                    bookId = bookId,
                    shortBook = shortBook,
                    needSaveScreenId = needSaveScreenId
                )
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

            is RootComponent.Screen.SingleBookParsingScreen -> {
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
    }

    override fun openModerationScreen() {
        (component.screenStack.value.active.instance as? RootComponent.Screen.AdminScreen)?.let {
            it.component.openModerationScreen()
        }
    }

    override fun openModerationBooksScreen() {
        (component.screenStack.value.active.instance as? RootComponent.Screen.ModerationScreen)?.let {
            it.component.openModerationBooksScreen()
        }
    }

    override fun closeBookInfoScreen() {
        component.activeScreenAsBookInfoOrNull()?.component?.onBackClicked()
    }

    override fun openAdminParsingBooksScreen() {
        (component.screenStack.value.active.instance as? RootComponent.Screen.AdminScreen)?.let {
            it.component.openParsingScreen()
        }
    }

    override fun openAdminPanel() {
        (component.screenStack.value.active.instance as? RootComponent.Screen.SettingsScreen)?.let {
            it.component.openAdminPanel()
        }
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
}