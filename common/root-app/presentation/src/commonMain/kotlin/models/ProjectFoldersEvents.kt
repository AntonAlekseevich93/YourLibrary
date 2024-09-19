package models

import BaseEvent

sealed class ProjectFoldersEvents : BaseEvent {
    data object RestartApp : ProjectFoldersEvents()
}