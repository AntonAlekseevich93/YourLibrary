package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface AdminScreenComponent {
    fun onBack()
    fun openParsingScreen()
    fun openModerationScreen()
}

class DefaultAdminScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onOpenParsingScreen: () -> Unit,
    val openModerationScreenCallback: () -> Unit,
) : AdminScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun openModerationScreen() {
        openModerationScreenCallback()
    }

    override fun openParsingScreen() {
        onOpenParsingScreen()
    }
}