package models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import base.BaseUIState

data class UserUiState(
   val isSignUnState: MutableState<Boolean> = mutableStateOf(false)
) : BaseUIState