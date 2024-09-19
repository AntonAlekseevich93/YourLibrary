package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface ModerationBooksScreenComponent {
    fun onBack()
}

class DefaultModerationBooksScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : ModerationBooksScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }
}