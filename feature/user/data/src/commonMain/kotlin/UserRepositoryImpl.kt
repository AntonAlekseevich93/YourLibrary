import database.LocalUserDataSource
import database.room.entities.user.toEntity
import database.room.entities.user.toVo
import kotlinx.coroutines.flow.Flow
import ktor.RemoteUserDataSource
import main_models.rest.base.BaseResponse
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.rest.users.AuthResponse
import main_models.rest.users.UserStatusVo
import main_models.rest.users.toVo
import main_models.user.UserTimestampVo
import main_models.user.UserVo

class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val appConfig: AppConfig,
) : UserRepository {

    override suspend fun getUserTimestamp(): UserTimestampVo =
        localUserDataSource.getUserTimestamp(appConfig.userId).toVo()

    override suspend fun updateUserTimestamp(userTimestampVo: UserTimestampVo) {
        localUserDataSource.updateUserTimestamp(userTimestampVo.toEntity())
    }

    override suspend fun getNotSynchronizedUser() =
        localUserDataSource.getNotSynchronizedUser(appConfig.userId)

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
        val userId = appConfig.userId
        remoteUserDataSource.getUser()?.toVo()?.let {
            localUserDataSource.createOrUpdateUser(
                id = it.id.toInt(),
                name = it.name,
                email = it.email,
                isVerified = it.isVerified,
                isAuthorized = true,
                timestamp = it.timestampOfUpdating,
            )

            it.userReadingGoalsInYears?.goals?.map { it.toEntity(userId) }?.let {
                localUserDataSource.updateUserReadingGoals(it)
            }
        }
    }

    override suspend fun updateUserInfo(userVo: UserVo) {
        val userId = appConfig.userId
        localUserDataSource.createOrUpdateUser(
            id = userVo.id.toInt(),
            name = userVo.name,
            email = userVo.email,
            isVerified = userVo.isVerified,
            isAuthorized = true,
            timestamp = userVo.timestampOfUpdating
        )
        userVo.userReadingGoalsInYears?.goals?.map { it.toEntity(userId) }?.let {
            localUserDataSource.updateUserReadingGoals(it)
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