import database.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import main_models.user.UserVo

class UserInfoProviderImpl(
    private val localUserDataSource: LocalUserDataSource
) : UserInfoProvider {
    override suspend fun getAuthorizedUser(): Flow<UserVo?> =
        localUserDataSource.getAuthorizedUser()
}