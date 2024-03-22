import base.BaseMVIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import models.UserUiState
import platform.Platform

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

        }
    }

}