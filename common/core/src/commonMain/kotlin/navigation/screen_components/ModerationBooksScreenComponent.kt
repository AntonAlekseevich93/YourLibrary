package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ModerationBooksScreenComponent {
    fun onBack()
    fun onCloseScreen()
}

class DefaultModerationBooksScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onCloseScreenListener: () -> Unit,
) : ModerationBooksScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }
}