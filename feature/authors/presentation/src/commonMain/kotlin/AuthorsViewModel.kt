import alert_dialog.CommonAlertDialogConfig
import androidx.compose.runtime.mutableStateOf
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import models.AuthorAlertDialogState
import models.AuthorsEvents
import models.AuthorsUiState
import models.JoiningAuthorsUiState
import navigation_drawer.contents.models.DrawerEvents
import platform.Platform
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents

class AuthorsViewModel(
    private val platform: Platform,
    private val interactor: AuthorsInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val drawerScope: DrawerScope,
) : BaseMVIViewModel<AuthorsUiState, BaseEvent>(AuthorsUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var joinAuthorsJob: Job? = null

    init {
        getAllAuthors()
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is ToolbarEvents.OnCloseEvent -> {
                uiStateValue.clearJoiningAuthors()
                navigationHandler.goBack()
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is AuthorsEvents.OpenJoinAuthorsScreen -> {
                getAllAuthorsNotSeparatingSimilarWithExceptionId(event.author)
                navigationHandler.navigateToJoinAuthorsScreen()
            }

            is AuthorsEvents.OnSearch -> searchAuthors(event.searchingAuthorName, event.exceptId)
            is AuthorsEvents.FinishSearch -> updateUIState(
                uiStateValue.copy(searchingAuthorResult = mutableStateOf(uiStateValue.joiningAuthorsUiState.value.allAuthorsExceptMainAndRelates))
            )

            is AuthorsEvents.AddAuthorToRelates -> addAuthorToRelates(
                originalAuthor = event.originalAuthor,
                modifiedAuthorId = event.modifiedAuthorId,
            )

            is AuthorsEvents.RemoveAuthorFromRelates -> removeAuthorFromRelates(
                originalAuthor = event.originalAuthor,
                modifiedAuthorId = event.modifiedAuthorId
            )

            is AuthorsEvents.SetAuthorAsMain -> {
                changeMainAuthor(
                    oldAuthorId = event.oldAuthorId,
                    newAuthorId = event.newAuthorId,
                    newMainAuthorName = event.newAuthorName
                )
            }

            is AuthorsEvents.RenameAuthor -> renameAuthor(
                authorId = event.authorId,
                newName = event.newName
            )

            is AuthorsEvents.HideAlertDialog -> updateUIState(
                uiStateValue.copy(
                    authorAlertDialogState = mutableStateOf(AuthorAlertDialogState())
                )
            )

            is AuthorsEvents.ShowAlertDialog -> {
                showAlertDialog(event.author, event.config)
            }
        }
    }

    fun getPlatform() = platform

    private fun getAllAuthors() {
        scope.launch {
            interactor.getAllAuthorsByAlphabet().collect { authors ->
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            authorByAlphabet = authors
                        )
                    )
                }
            }
        }
    }

    private fun addAuthorToRelates(
        originalAuthor: AuthorVo,
        modifiedAuthorId: String,
    ) {
        scope.launch {
            interactor.addAuthorToRelates(
                originalAuthorId = originalAuthor.id,
                originalAuthorName = originalAuthor.name,
                modifiedAuthorId = modifiedAuthorId,
            )
            getMainAuthorById(originalAuthor.id)
        }
    }

    private fun removeAuthorFromRelates(
        originalAuthor: AuthorVo,
        modifiedAuthorId: String
    ) {
        scope.launch {
            interactor.removeAuthorFromRelates(
                originalAuthorId = originalAuthor.id
            )
            getMainAuthorById(modifiedAuthorId)
        }
    }

    private fun searchAuthors(searchingAuthorName: String, exceptId: String) {
        scope.launch {
            //todo добавлен поиск на бэке
//            val result = interactor.searchAuthorExceptId(
//                searchingAuthorName, exceptId
//            )
//            withContext(Dispatchers.Main) {
//                updateUIState(uiStateValue.copy(searchingAuthorResult = mutableStateOf(result)))
//            }
        }
    }

    private fun getAllAuthorsNotSeparatingSimilarWithExceptionId(author: AuthorVo) {
        joinAuthorsJob?.cancel()
        joinAuthorsJob = scope.launch {
            interactor.getAllAuthorsNotSeparatingSimilarWithExceptionId(author.id).collect {
                withContext(Dispatchers.Main) {
                    updateUIState(
                        uiStateValue.copy(
                            joiningAuthorsUiState = mutableStateOf(
                                JoiningAuthorsUiState(
                                    mainAuthor = mutableStateOf(author),
                                    allAuthorsExceptMainAndRelates = it
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun changeMainAuthor(
        oldAuthorId: String,
        newAuthorId: String,
        newMainAuthorName: String
    ) {
        scope.launch {
            interactor.changeMainAuthor(
                oldAuthorId = oldAuthorId,
                newAuthorId = newAuthorId,
                newMainAuthorName = newMainAuthorName
            )
            getMainAuthorById(newAuthorId)
        }
    }

    private suspend fun getMainAuthorById(authorId: String) {
        interactor.getMainAuthorById(authorId)?.let { newAuthor ->
            getAllAuthorsNotSeparatingSimilarWithExceptionId(newAuthor)
        }
    }

    private fun renameAuthor(authorId: String, newName: String) {
        scope.launch {
            val isSuccess = interactor.renameAuthor(authorId = authorId, newName = newName)
            if (isSuccess) {
                getMainAuthorById(uiStateValue.joiningAuthorsUiState.value.mainAuthor.value.id)
            } else {
                uiStateValue.snackbarHostState.value.showSnackbar(Strings.error_author_name_exist)
            }
        }
    }

    private fun showAlertDialog(author: AuthorVo, config: CommonAlertDialogConfig) {
        updateUIState(
            uiStateValue.copy(
                authorAlertDialogState = mutableStateOf(
                    AuthorAlertDialogState(
                        show = true,
                        selectedAuthor = author,
                        config = config
                    )
                )
            )
        )
    }

}