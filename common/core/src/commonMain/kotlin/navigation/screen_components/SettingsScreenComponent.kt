package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface SettingsScreenComponent {
    fun onBack()
}

class DefaultSettingsScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : SettingsScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }
}