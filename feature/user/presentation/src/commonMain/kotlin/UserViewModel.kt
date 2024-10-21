import androidx.compose.runtime.mutableStateOf
import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
) : BaseMVIViewModel<UserUiState, BaseEvent>(UserUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var userRefreshJob: Job? = null

    init {
        getUserInfo()
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
            is UserEvents.ChangeBooksGoal -> updateUserReadingGoalsInCurrentYear(event.goal)
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

    private fun getUserInfo() {
        uiStateValue.showAdminPanel.value = appConfig.isModerator
        scope.launch(Dispatchers.IO) {
            launch {
                interactor.getAuthorizedUser().collect { user ->
                    user?.let {
                        withContext(Dispatchers.Main) {
                            updateUIState(
                                uiStateValue.copy(
                                    userInfo = mutableStateOf(user),
                                )
                            )
                        }
                    }
                }
            }
            launch {
                interactor.getUserBooksStatistics().collect {
                    withContext(Dispatchers.Main) {
                        uiStateValue.userBooksStatistics.value = it
                    }
                }
            }

            launch {
                interactor.getFinishedThisYearCountBooks().collect {
                    withContext(Dispatchers.Main) {
                        uiStateValue.finishedThisYearBooksCount.value = it
                    }
                }
            }

            launch {
                interactor.getUserReviews().collect {
                    withContext(Dispatchers.Main) {
                        uiStateValue.userReviews.value = it
                    }
                }
            }
        }
    }

    private fun updateUserReadingGoalsInCurrentYear(newGoal: Int) {
        scope.launch {
            interactor.updateUserReadingGoalsInCurrentYear(newGoal)
        }
    }

}