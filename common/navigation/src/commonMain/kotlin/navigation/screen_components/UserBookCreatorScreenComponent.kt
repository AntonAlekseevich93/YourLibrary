package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface UserBookCreatorScreenComponent {
    fun onBack()
    fun toMain()

}

class DefaultUserBookCreatorScreenComponent(
    componentContext: ComponentContext,
    val onBackListener: () -> Unit,
    val toMainListener: () -> Unit,
) : UserBookCreatorScreenComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackListener()
    }

    override fun toMain() {
        toMainListener()
    }
}