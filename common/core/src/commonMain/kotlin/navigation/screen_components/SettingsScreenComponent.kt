package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface SettingsScreenComponent {
    fun onBack()

    fun openAdminPanel()
}

class DefaultSettingsScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onOpenAdminPanelListener: () -> Unit,
) : SettingsScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun openAdminPanel() {
        onOpenAdminPanelListener()
    }
}