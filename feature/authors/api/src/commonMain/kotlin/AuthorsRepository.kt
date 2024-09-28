import main_models.AuthorVo
import main_models.authors.AuthorTimestampVo

interface AuthorsRepository {
    suspend fun updateAuthorsTimestamp(
        thisDeviceTimestamp: Long?,
        otherDeviceTimestamp: Long?
    )

    suspend fun updateAuthorInLocalDb(author: AuthorVo)
    suspend fun insertOrUpdateAuthorsInLocalDb(authors: List<AuthorVo>)
    suspend fun getAuthorsTimestamp(userId: Int): AuthorTimestampVo
    suspend fun getNotSynchronizedAuthors(): List<AuthorVo>
    suspend fun createAuthorIfNotExist(author: AuthorVo)
    suspend fun getLocalAuthorById(authorId: String): AuthorVo?
}