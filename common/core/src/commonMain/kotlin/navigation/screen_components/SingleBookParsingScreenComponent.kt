package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface SingleBookParsingScreenComponent {
    fun onBack()
    fun onCloseScreen()
}

class DefaultSingleBookParsingScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onCloseScreenListener: () -> Unit,
) : SingleBookParsingScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }
}