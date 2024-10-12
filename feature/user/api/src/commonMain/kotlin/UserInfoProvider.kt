import kotlinx.coroutines.flow.Flow
import main_models.user.UserVo

/**created because it is impossible to use UserRepository due to cyclic dependency**/
interface UserInfoProvider {
    suspend fun getAuthorizedUser(): Flow<UserVo?>
}