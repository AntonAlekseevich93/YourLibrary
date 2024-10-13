package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface ParsingScreenComponent {
    fun onBack()
    fun onCloseScreen()
    fun openSingleParsingScreen()
}

class DefaultParsingScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val onCloseScreenListener: () -> Unit,
    val onOpenSingleBookParsingScreen: () -> Unit,
) : ParsingScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

    override fun onCloseScreen() {
        onCloseScreenListener()
    }

    override fun openSingleParsingScreen() {
        onOpenSingleBookParsingScreen()
    }
}