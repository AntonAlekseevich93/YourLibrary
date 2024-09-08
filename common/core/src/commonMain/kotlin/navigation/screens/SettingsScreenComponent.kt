package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface SettingsScreenComponent {

}

class DefaultSettingsScreenComponent(
    componentContext: ComponentContext,
) : SettingsScreenComponent, ComponentContext by componentContext {

}