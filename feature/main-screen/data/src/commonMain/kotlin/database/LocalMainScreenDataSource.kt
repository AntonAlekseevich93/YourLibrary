package database

import kotlinx.coroutines.flow.Flow
import main_models.path.PathInfoDto

class LocalMainScreenDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun getSelectedPathInfo(): Flow<PathInfoDto?> = db.getSelectedPathInfo()

}