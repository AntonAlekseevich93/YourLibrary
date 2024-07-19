import main_models.AuthorVo
import main_models.authors.AuthorTimestampVo

interface AuthorsRepository {
    suspend fun updateThisDeviceAuthorsTimestamp(
        thisDeviceTimestamp: Long?,
        otherDeviceTimestamp: Long?
    )

    suspend fun updateAuthorInLocalDb(author: AuthorVo)
    suspend fun addAuthorsToLocalDb(authors: List<AuthorVo>)
    suspend fun getAuthorsTimestamp(userId: Long): AuthorTimestampVo
}