package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ModerationScreenComponent {
    fun openModerationBooksScreen()
    fun onBack()
    fun onCloseScreen()
}

class DefaultModerationScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val openModerationBooksScreenEvent: () -> Unit,
    val onCloseScreenListener: () -> Unit,
) : ModerationScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun openModerationBooksScreen() {
        openModerationBooksScreenEvent()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }
}