package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface ModerationScreenComponent {
    fun openModerationBooksScreen()
    fun onBack()
}

class DefaultModerationScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val openModerationBooksScreenEvent: () -> Unit,
) : ModerationScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun openModerationBooksScreen() {
        openModerationBooksScreenEvent()
    }
}