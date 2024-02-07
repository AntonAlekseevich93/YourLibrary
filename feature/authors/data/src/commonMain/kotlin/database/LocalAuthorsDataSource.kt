package database

import DatabaseUtils.Companion.toAuthorLocalDto
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

    suspend fun getAllMainAuthors(): List<AuthorLocalDto> =
        db.appQuery.getAllMainAuthors().executeAsList().map { it.toAuthorLocalDto() }


}