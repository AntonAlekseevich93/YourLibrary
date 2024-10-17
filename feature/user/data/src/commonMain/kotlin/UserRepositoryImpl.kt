import database.LocalUserDataSource
import database.room.entities.user.toEntity
import kotlinx.coroutines.flow.Flow
import ktor.RemoteUserDataSource
import ktor.models.toVo
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.rest.users.UserStatusVo
import main_models.rest.users.toVo
import main_models.user.UserVo

class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val appConfig: AppConfig,
) : UserRepository {

    override suspend fun signUp(
        request: AuthRegisterRequest
    ): BaseResponse<AuthResponse, String>? = remoteUserDataSource.signUp(
        request = request
    )

    override suspend fun signIn(
        request: AuthRequest
    ): BaseResponse<AuthResponse, String>? = remoteUserDataSource.signIn(
        request = request
    )

    override suspend fun getUserStatus(): UserStatusVo? =
        remoteUserDataSource.getUserStatus()?.toVo()

    override suspend fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Boolean,
        isAuthorized: Boolean
    ) {
        localUserDataSource.createOrUpdateUser(
            id = id,
            name = name,
            email = email,
            isVerified = isVerified,
            isAuthorized = isAuthorized,
            timestamp = 0//todo fix
        )
    }

    override suspend fun getAuthorizedUser(): Flow<UserVo?> =
        localUserDataSource.getAuthorizedUser()

    override suspend fun logOut() {
        localUserDataSource.logOut()
    }

    override suspend fun updateUserInfo() {
        remoteUserDataSource.getUser()?.toVo()?.let {
            localUserDataSource.createOrUpdateUser(
                id = it.id.toInt(),
                name = it.name,
                email = it.email,
                isVerified = it.isVerified,
                isAuthorized = true,
                timestamp = 0//todo fix
            )
        }
    }

    override suspend fun updateUserReadingGoalsInCurrentYear(goal: Int) {
        remoteUserDataSource.updateUserBooksGoal(goal)?.toVo()?.let {
            val userId = appConfig.userId
            localUserDataSource.updateUser(it.toEntity())
            it.userReadingGoalsInYears?.goals?.let { goals ->
                localUserDataSource.updateUserReadingGoals(goals.map { it.toEntity(userId) })
            }
        }
    }

}