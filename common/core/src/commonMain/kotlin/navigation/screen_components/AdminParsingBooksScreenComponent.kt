package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface AdminParsingBooksScreenComponent {
    fun onBack()
}

class DefaultAdminParsingBooksScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : AdminParsingBooksScreenComponent, ComponentContext by componentContext {
    override fun onBack() {
        onBackListener()
    }
}