package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface SettingsScreenComponent {
    fun openModerationScreen()
}

class DefaultSettingsScreenComponent(
    componentContext: ComponentContext,
    val openModerationScreenCallback: () -> Unit,
) : SettingsScreenComponent, ComponentContext by componentContext {
    override fun openModerationScreen() {
        openModerationScreenCallback()
    }
}