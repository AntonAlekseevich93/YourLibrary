package main_app_bar

import BaseEvent

sealed class MainAppBarEvents : BaseEvent {
    data class OnSearch(val text: String) : MainAppBarEvents()
}