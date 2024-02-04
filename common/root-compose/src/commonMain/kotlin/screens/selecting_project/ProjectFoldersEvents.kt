package screens.selecting_project

import BaseEvent
import main_models.path.PathInfoVo

sealed class ProjectFoldersEvents : BaseEvent {
    class SelectFolderEvent(val path: String) : ProjectFoldersEvents()

    class CreateFolder(val path: String, val name: String) : ProjectFoldersEvents()

    class SelectPathInfo(val pathInfo: PathInfoVo) : ProjectFoldersEvents()

    class RenamePath(val pathInfo: PathInfoVo, val newName: String) : ProjectFoldersEvents()
    data object RestartApp : ProjectFoldersEvents()
}