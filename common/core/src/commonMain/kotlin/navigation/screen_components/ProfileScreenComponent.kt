package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ProfileScreenComponent {
    fun onSettingsClick()
    fun openAdminPanel()
}

class DefaultProfileScreenComponent(
    componentContext: ComponentContext,
    val onSettingsClickListener: () -> Unit,
    val onOpenAdminPanelListener: () -> Unit,
) : ProfileScreenComponent, ComponentContext by componentContext {

    override fun onSettingsClick() {
        onSettingsClickListener()
    }

    override fun openAdminPanel() {
        onOpenAdminPanelListener()
    }
}