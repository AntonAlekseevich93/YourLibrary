package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface UserBookCreatorScreenComponent {
    fun onBack()

}

class DefaultUserBookCreatorScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
) : UserBookCreatorScreenComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackListener()
    }
}