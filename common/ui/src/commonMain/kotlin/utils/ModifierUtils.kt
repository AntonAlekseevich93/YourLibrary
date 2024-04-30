package utils

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * The extension allows you to prevent double clicks
 */
fun Modifier.debounceClick(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    debounceMillis: Long = 300,
    onClick: () -> Unit,
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val debounceJob = remember { mutableStateOf<Job?>(null) }

    this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled
    ) {
        debounceJob.value?.cancel()
        debounceJob.value = scope.launch {
            delay(debounceMillis)
            onClick.invoke()
        }
    }
}