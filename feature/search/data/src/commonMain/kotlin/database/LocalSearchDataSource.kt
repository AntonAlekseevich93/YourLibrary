package database

import DatabaseUtils.Companion.toAuthorLocalDto
import main_models.local_models.AuthorLocalDto

class LocalSearchDataSource(
    private val db: SqlDelightDataSource
) {
    suspend fun getAllMatchesByAuthorName(name: String): List<AuthorLocalDto> =
        db.appQuery.getAllAuthorByName("%${name.uppercase()}%").executeAsList()
            .map { item -> item.toAuthorLocalDto() }
}