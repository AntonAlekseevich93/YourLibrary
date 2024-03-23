import androidx.compose.runtime.mutableStateOf
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import models.UserEvents
import models.UserUiState
import platform.Platform
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents

class UserViewModel(
    private val platform: Platform,
    private val interactor: UserInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val drawerScope: DrawerScope,
) : BaseMVIViewModel<UserUiState, BaseEvent>(UserUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var joinAuthorsJob: Job? = null

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is ToolbarEvents.OnCloseEvent -> {
                navigationHandler.goBack()
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
            is UserEvents.OnSignUpClick -> updateUIState(
                uiStateValue.copy(isSignUnState = mutableStateOf(true))
            )

            is UserEvents.OnSignInClick -> updateUIState(
                uiStateValue.copy(isSignUnState = mutableStateOf(false))
            )

            is UserEvents.OnSignUpConfirmClick -> {

            }

            is UserEvents.OnSignInConfirmClick -> {

            }
        }
    }

}