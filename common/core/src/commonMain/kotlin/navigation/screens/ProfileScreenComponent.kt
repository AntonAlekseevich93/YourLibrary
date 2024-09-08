package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface ProfileScreenComponent {

}

class DefaultProfileScreenComponent(
    componentContext: ComponentContext,
) : ProfileScreenComponent, ComponentContext by componentContext {

}