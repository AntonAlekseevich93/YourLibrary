import database.LocalUserDataSource
import database.room.entities.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.user.UserVo

class UserInfoProviderImpl(
    private val localUserDataSource: LocalUserDataSource
) : UserInfoProvider {
    override suspend fun getAuthorizedUser(): Flow<UserVo?> =
        localUserDataSource.getAuthorizedUser().map { it?.toVo() }
}