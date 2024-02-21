package database

import DatabaseUtils.Companion.toAuthorLocalDto
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.local_models.AuthorLocalDto

class LocalAuthorsDataSource(
    private val db: SqlDelightDataSource
) {

    suspend fun createAuthor(author: AuthorLocalDto) {
        author.apply {
            if (id != null && name != null && uppercaseName != null && isMainAuthor != null && timestampOfCreating != null
                && timestampOfUpdating != null
            ) {
                db.appQuery.createAuthor(
                    id = id!!,
                    name = name!!,
                    uppercaseName = uppercaseName!!,
                    relatedToAuthorId = relatedToAuthorId,
                    isMainAuthor = isMainAuthor!!.toLong(),
                    timestampOfCreating = timestampOfCreating!!,
                    timestampOfUpdating = timestampOfUpdating!!
                )
            }
        }
    }

    fun changeMainAuthor(oldMainAuthorId: String, newMainAuthorId: String) {
        db.appQuery.disableMainAuthor(oldMainAuthorId)
        db.appQuery.setAsMainAuthor(newMainAuthorId)
        db.appQuery.updateRelatedToAuthorId(newMainAuthorId, oldMainAuthorId)
    }

    fun getAllRelatedAuthors(mainAuthorId: String): List<AuthorLocalDto> =
        db.appQuery.getAllRelatedAuthors(mainAuthorId).executeAsList().map { it.toAuthorLocalDto() }

    fun getAuthorById(id: String): AuthorLocalDto? =
        db.appQuery.getAuthorById(id).executeAsList().firstOrNull()?.toAuthorLocalDto()

    suspend fun getAllMainAuthors(): Flow<List<AuthorLocalDto>> =
        db.appQuery.getAllMainAuthors().asFlow().mapToList(Dispatchers.IO)
            .map { it.map { it.toAuthorLocalDto() } }

    suspend fun getAllAuthors(): Flow<List<AuthorLocalDto>> =
        db.appQuery.getAllAuthors().asFlow().mapToList(Dispatchers.IO)
            .map { it.map { it.toAuthorLocalDto() } }

    suspend fun addAuthorToRelates(
        originalAuthorId: String,
        originalAuthorName: String,
        modifiedAuthorId: String,
    ) {
        db.appQuery.updateRelationToAuthor(originalAuthorId, modifiedAuthorId)
        db.appQuery.setAsNotMainAuthor(modifiedAuthorId)
        db.appQuery.setModifierAuthor(
            modifiedAuthorId = originalAuthorId,
            modifiedAuthorName = originalAuthorName,
            originalAuthorId = modifiedAuthorId
        )
    }

    fun removeAuthorFromRelates(originalAuthorId: String) {
        db.appQuery.removeAuthorFromRelates(originalAuthorId)
        db.appQuery.setAsMainAuthor(originalAuthorId)
        db.appQuery.clearModifierAuthor(originalAuthorId)
    }


}