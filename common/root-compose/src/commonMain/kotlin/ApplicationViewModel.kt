import database.SqlDelightDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.PathInfoVo
import platform.Platform
import sub_app_bar.ViewsType
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ApplicationViewModel(private val db: SqlDelightDataSource) {
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

    fun switchViewTypesListener(isChecked: Boolean, viewsType: ViewsType) {
        _uiState.value.changeViewTypes(isChecked, viewsType)
    }

    fun changeViewsTypes() {
        _uiState.value.applyCheckedViewTypes()
        //todo здесь изменить экран который открыт если этого item больше нет
    }

    fun openViewType(viewsType: ViewsType) {
        _uiState.value.openedViewType.value = viewsType
    }

    fun isDbPathIsExist(platform: Platform): Boolean {
        val isExist = db.isPathIsExist(platform)
        if (isExist && db.appDbIsNotInitialized) {
            db.initializeAppDatabase()
        }
        return isExist
    }

    fun createDbPath(dbPath: String, libraryName: String): Int? {
        val id = db.createDbPath(dbPath, libraryName)
        if (db.appDbIsNotInitialized) {
            db.initializeAppDatabase()
        }
        return id
    }

    fun selectPathInfo(pathInfo: PathInfoVo) {
        if (!pathInfo.isSelected) {
            db.setPathAsSelected(pathInfo.id)
        }
    }

    fun createFolderAndGetPath(path: String, name: String): String? = try {
        File(path, name).mkdir()
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        val selectedPathResult =
            if (path.last() == osDivider.first()) path else "$path$osDivider"
        selectedPathResult + name + osDivider
    } catch (_: Exception) {
        null
    }

    fun renamePath(pathInfo: PathInfoVo, newName: String): String {
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

    suspend fun setFolderAsSelected(path: String, libraryName: String): Boolean {
        createDbPath(dbPath = path, libraryName = libraryName)?.let { id ->
            db.setPathAsSelected(id)
            return true
        }
        return false
    }

    fun getPathByOs(path: String): String {
        val osDivider =
            if (path.contains("""\""")) """\""" else """/"""
        return if (path.last() == osDivider.first()) path else "$path$osDivider"
    }

    private suspend fun renamePathInDb(path: String, pathId: Int, newName: String) {
        db.renamePath(pathId, path, newName)
    }
}