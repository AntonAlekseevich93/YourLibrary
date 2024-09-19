package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface ModerationScreenComponent {
    fun onBack()
}

class DefaultModerationScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : ModerationScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }
}