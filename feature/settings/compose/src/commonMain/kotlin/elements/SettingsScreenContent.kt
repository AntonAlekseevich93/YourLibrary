package elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import models.settings_items.DebugSettingsItem
import models.settings_items.SettingsItem

@Composable
fun SettingsScreenContent(state: State<List<SettingsItem>>) {
    state.value.forEach { item ->
        when (item) {
            is DebugSettingsItem -> {}
        }
    }
}