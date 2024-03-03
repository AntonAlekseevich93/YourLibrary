package models

import BaseEvent

sealed class SettingsEvents : BaseEvent {
    data object CloseSettingsScreen : SettingsEvents()
}