package models

import BaseEvent

sealed class SettingsEvents : BaseEvent {
    data object OnOpenAdminPanel : SettingsEvents()
    data object ClearAllCache : SettingsEvents()
}