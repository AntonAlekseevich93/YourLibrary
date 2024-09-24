package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ModerationBooksCoversScreenComponent {
    fun onBack()
    fun onCloseScreen()
}

class DefaultModerationBooksCoversScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onCloseScreenListener: () -> Unit,
) : ModerationBooksCoversScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }
}