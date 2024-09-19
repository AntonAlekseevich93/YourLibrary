package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface SingleBookParsingScreenComponent {
    fun onBack()
}

class DefaultSingleBookParsingScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : SingleBookParsingScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }

}