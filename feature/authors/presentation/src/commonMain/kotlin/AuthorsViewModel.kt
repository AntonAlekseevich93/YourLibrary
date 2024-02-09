import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import models.AuthorsUiState
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

    init {
        getAllAuthors()
    }

    override fun sendEvent(event: BaseEvent) {
        when(event){
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is ToolbarEvents.OnCloseEvent -> navigationHandler.goBack()
            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
        }
    }

    private fun getAllAuthors() {
        scope.launch {
            val authors = interactor.getAllAuthorsByAlphabet()
            updateUIState(
                uiStateValue.copy(
                    authorByAlphabet = authors
                )
            )
        }
    }

}