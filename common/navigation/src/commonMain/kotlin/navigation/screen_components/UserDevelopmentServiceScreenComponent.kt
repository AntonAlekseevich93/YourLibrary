package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface UserDevelopmentServiceScreenComponent {
    fun openBookInfoScreen(localId: Long?)
    fun openServiceDevelopmentBookEditScreen()

    fun onBack()
}

class DefaultUserDevelopmentServiceScreenComponent(
    componentContext: ComponentContext,
    val bookInfoScreenListener: (localId: Long?) -> Unit,
    val serviceDevelopmentBookEditScreen: () -> Unit,
    val onBackClicked: () -> Unit,
) : UserDevelopmentServiceScreenComponent, ComponentContext by componentContext {

    override fun openBookInfoScreen(localId: Long?) {
        bookInfoScreenListener(localId)
    }

    override fun openServiceDevelopmentBookEditScreen() {
        serviceDevelopmentBookEditScreen()
    }

    override fun onBack() {
        onBackClicked()
    }
}