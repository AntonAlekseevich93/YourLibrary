package base

import BaseEvent
import BaseEventScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface BaseUIState
abstract class BaseMVIViewModel<
        State : BaseUIState,
        Event : BaseEvent
        >(defaultState: State) : BaseEventScope<Event> {

    private val _uiState = MutableStateFlow(defaultState)
    val uiState = _uiState.asStateFlow()
    protected val uiStateValue
        get() = uiState.value

    protected fun updateUIState(state: State) {
        _uiState.value = state
    }
}