package menu_bar

import BaseEvent

sealed class LeftMenuBarEvents : BaseEvent {
    data object OnSearchClickEvent : LeftMenuBarEvents()
    data object OnCreateBookClickEvent : LeftMenuBarEvents()
    data object OnSelectAnotherVaultEvent : LeftMenuBarEvents()
}