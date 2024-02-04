import database.SqlDelightDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.TooltipItem
import main_models.path.PathInfoVo
import models.SettingsDataProvider
import platform.Platform
import screens.selecting_project.ProjectFoldersEvents
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ApplicationViewModel(
    private val db: SqlDelightDataSource,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val settingsDataProvider: SettingsDataProvider,
) : BaseEventScope<BaseEvent>, ApplicationScope, DrawerScope {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState

    init {
        scope.launch {
            db.getAllPathInfo().collect { pathInfo ->
                if (pathInfo != null) {
                    withContext(Dispatchers.Main) {
                        _uiState.value.addPathInfo(pathInfo)
                    }
                }
            }
        }
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is ProjectFoldersEvents.SelectFolderEvent -> selectFolder(event.path)
            is ProjectFoldersEvents.CreateFolder -> createFolder(event.path, event.name)
            is ProjectFoldersEvents.SelectPathInfo -> {
                selectPathInfo(event.pathInfo)
                navigationHandler.toMain()
            }

            is ProjectFoldersEvents.RenamePath -> {
                val newPath = renamePath(
                    pathInfo = event.pathInfo,
                    newName = event.newName,
                )
                settingsDataProvider.updateLibraryNameInFile(
                    path = newPath,
                    oldName = event.pathInfo.libraryName,
                    newName = event.newName
                )
            }

            is ProjectFoldersEvents.RestartApp -> navigationHandler.restartWindow()
        }
    }

    override fun closeBookScreen() {
        _uiState.value.apply {
            fullScreenBookInfo.value = false
            navigationHandler.goBack()
        }
    }

    override fun openLeftDrawerOrClose() {
        _uiState.value.apply {
            if (!showLeftDrawerState.value) {
                showLeftDrawerState.value = true
                openLeftDrawerEvent.value.invoke()
            } else {
                showLeftDrawerState.value = false
                closeLeftDrawerEvent.value.invoke()
            }
        }
    }

    override fun openRightDrawerOrClose() {
        _uiState.value.apply {
            if (!showRightDrawerState.value) {
                showRightDrawerState.value = true
                openRightDrawerEvent.value.invoke()
            } else {
                showRightDrawerState.value = false
                closeRightDrawerEvent.value.invoke()
            }
        }
    }

    override fun setTooltip(tooltip: TooltipItem) {
        tooltipHandler.setTooltip(tooltip)
    }

    fun isDbPathIsExist(platform: Platform): Boolean {
        val isExist = db.isPathIsExist(platform)
        if (isExist && db.appDbIsNotInitialized) {
            db.initializeAppDatabase()
        }
        return isExist
    }

    private fun selectPathInfo(pathInfo: PathInfoVo) {
        if (!pathInfo.isSelected) {
            db.setPathAsSelected(pathInfo.id)
        }
    }

    private fun renamePath(pathInfo: PathInfoVo, newName: String): String {
        val pathString = pathInfo.path.replace(pathInfo.libraryName, newName)
        scope.launch {
            try {
                val oldPath = Paths.get(pathInfo.path)
                val newPath = Paths.get(pathString)
                Files.move(
                    oldPath,
                    newPath,
                    StandardCopyOption.REPLACE_EXISTING
                )
                renamePathInDb(pathString, pathInfo.id, newName)
            } catch (_: Throwable) {
                //todo log
            }
        }
        return pathString
    }

    private suspend fun setFolderAsSelected(path: String, libraryName: String): Boolean {
        createDbPath(dbPath = path, libraryName = libraryName)?.let { id ->
            db.setPathAsSelected(id)
            return true
        }
        return false
    }

    private suspend fun renamePathInDb(path: String, pathId: Int, newName: String) {
        db.renamePath(pathId, path, newName)
    }

    private fun selectFolder(path: String) {
        scope.launch {
            getPathByOs(path).let { osPath ->
                settingsDataProvider.getLibraryNameIfExist(osPath)?.let { libraryName ->
                    val isSuccess = setFolderAsSelected(
                        path = osPath,
                        libraryName = libraryName
                    )
                    if (isSuccess) {
                        navigationHandler.toMain()
                    }
                }
            }
        }
    }

    private fun createDbPath(dbPath: String, libraryName: String): Int? {
        val id = db.createDbPath(dbPath, libraryName)
        if (db.appDbIsNotInitialized) {
            db.initializeAppDatabase()
        }
        return id
    }

    private fun getPathByOs(path: String): String {
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        return if (path.last() == osDivider.first()) path else "$path$osDivider"
    }

    private fun createFolderAndGetPath(path: String, name: String): String? = try {
        File(path, name).mkdir()
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        val selectedPathResult =
            if (path.last() == osDivider.first()) path else "$path$osDivider"
        selectedPathResult + name + osDivider
    } catch (_: Exception) {
        null
    }

    private fun createFolder(path: String, name: String) {
        createFolderAndGetPath(path, name)?.let { resultPath ->
            settingsDataProvider.createAppSettingsFile(
                path = resultPath,
                libraryName = name,
                themeName = "Dark" //todo
            )
            createDbPath(
                dbPath = resultPath,
                libraryName = name
            )
            navigationHandler.toMain()
        }
    }
}