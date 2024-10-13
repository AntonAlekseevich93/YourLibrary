package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ProfileScreenComponent {
    fun onSettingsClick()
    fun openAdminPanel()
    fun openServiceDevelopmentScreen()
}

class DefaultProfileScreenComponent(
    componentContext: ComponentContext,
    val onSettingsClickListener: () -> Unit,
    val onOpenAdminPanelListener: () -> Unit,
    val onOpenServiceDevelopmentScreenListener: () -> Unit,
) : ProfileScreenComponent, ComponentContext by componentContext {

    override fun onSettingsClick() {
        onSettingsClickListener()
    }

    override fun openAdminPanel() {
        onOpenAdminPanelListener()
    }

    override fun openServiceDevelopmentScreen() {
        onOpenServiceDevelopmentScreenListener()
    }
}