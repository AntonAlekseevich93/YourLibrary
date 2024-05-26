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
                //todo if need
                navigationHandler.navigateToJoinAuthorsScreen()
            }

            is AuthorsEvents.OnSearch -> searchAuthors(event.searchingAuthorName, event.exceptId)
            is AuthorsEvents.FinishSearch -> updateUIState(
                uiStateValue.copy(searchingAuthorResult = mutableStateOf(uiStateValue.joiningAuthorsUiState.value.allAuthorsExceptMainAndRelates))
            )

            is AuthorsEvents.AddAuthorToRelates -> {
                //todo if need
            }

            is AuthorsEvents.RemoveAuthorFromRelates -> {
                //todo if need
            }

            is AuthorsEvents.SetAuthorAsMain -> {
                //todo if need
            }

            is AuthorsEvents.RenameAuthor -> {
                //todo if need
            }

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