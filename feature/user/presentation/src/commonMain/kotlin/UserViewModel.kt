import androidx.compose.runtime.mutableStateOf
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.UserEvents
import models.UserUiState
import platform.Platform
import tooltip_area.TooltipEvents

class UserViewModel(
    private val platform: Platform,
    private val interactor: UserInteractor,
    private val tooltipHandler: TooltipHandler,
    private val drawerScope: DrawerScope,
    private val appConfig: AppConfig,
) : BaseMVIViewModel<UserUiState, BaseEvent>(UserUiState(appConfig = appConfig)) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var userRefreshJob: Job? = null

    init {
        scope.launch {
            launch {
                interactor.getAuthorizedUser().collect { user ->
                    user?.let {
                        updateUIState(
                            uiStateValue.copy(
                                userInfo = mutableStateOf(user),
                            )
                        )
                    }
                }
            }
            launch {
                uiStateValue.showAdminPanel.value = appConfig.isModerator
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is UserEvents.OnSignUpClick -> updateUIState(
                uiStateValue.copy(isSignUnState = mutableStateOf(true))
            )

            is UserEvents.OnSignInClick -> updateUIState(
                uiStateValue.copy(isSignUnState = mutableStateOf(false))
            )

            is UserEvents.OnSignUpConfirmClick -> signUp(event.name, event.email, event.password)

            is UserEvents.OnSignInConfirmClick -> signIn(event.email, event.password)
            is UserEvents.OnSignOut -> signOut()
            is UserEvents.GetUserIfVerified -> getUserIfVerified()
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        scope.launch(Dispatchers.IO) {
            interactor.signUp(name = name, email = email, password = password)
        }
    }

    private fun signIn(email: String, password: String) {
        scope.launch(Dispatchers.IO) {
            interactor.signIn(email = email, password = password)
        }
    }

    private fun signOut() {
        scope.launch(Dispatchers.IO) {
            interactor.logOut()
        }
    }

    private fun getUserIfVerified() {
        userRefreshJob?.cancel()
        userRefreshJob = scope.launch {
            delay(1000)
            interactor.updateUserInfo()
        }
    }

}