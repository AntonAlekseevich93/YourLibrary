package database

import database.room.RoomMainDataSource
import database.room.entities.user.UserEntity
import database.room.entities.user.UserGoalInYearEntity
import database.room.entities.user.UserTimestampEntity
import database.room.entities.user.UserWithGoals
import database.room.entities.user.toVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import main_models.user.UserReadingGoalsInYearsVo
import main_models.user.UserVo
import platform.PlatformInfoData
import java.util.Calendar

class LocalUserDataSource(
    roomDb: RoomMainDataSource,
    private val platformInfo: PlatformInfoData,
) {
    private val usersDao = roomDb.usersDao
    private val userTimestampDao = roomDb.userTimestampDao
    private val userGoalInYearDao = roomDb.userGoalInYearDao

    suspend fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Boolean,
        isAuthorized: Boolean,
        isModerator: Boolean,
        timestamp: Long,
    ) {
        val user = usersDao.getUserByEmail(email).firstOrNull()
        if (user == null) {
            usersDao.insertUser(
                UserEntity(
                    userId = id,
                    name = name,
                    email = email,
                    isVerified = isVerified,
                    isAuthorized = isAuthorized,
                    isModerator = isModerator,
                    timestampOfUpdating = timestamp
                )
            )
        } else {
            val updatedUser = user.copy(
                name = name,
                isVerified = isVerified,
                isAuthorized = isAuthorized
            )
            usersDao.updateUser(updatedUser)
        }
    }

    suspend fun updateUser(user: UserEntity) {
        val searchedUser = usersDao.getUserByEmail(user.email).firstOrNull()
        if (searchedUser == null) {
            usersDao.insertUser(user)
        } else {
            usersDao.updateUser(user)
        }
    }

    suspend fun getAuthorizedUserFlow(): Flow<UserVo?> = flow {
        val year = platformInfo.getCurrentTime().get(Calendar.YEAR)
        usersDao.getAuthorizedUser().collect {
            val users = it.mapToUsersVo(year).filter { it.isAuth }
            if (users.size > 1) {
                usersDao.setAllAsUnauthorized()
            } else {
                emit(users.firstOrNull())
            }
        }
    }

    suspend fun logOut() {
        usersDao.setAllAsUnauthorized()
    }

    suspend fun updateUserReadingGoals(goals: List<UserGoalInYearEntity>) {
        goals.map {
            userGoalInYearDao.insertOrUpdateUserGoal(it)
        }
    }

    suspend fun getUserTimestamp(userId: Int): UserTimestampEntity {
        val timestamp = userTimestampDao.getTimestamp(userId).firstOrNull()
        return timestamp ?: createEmptyTimestamp(userId)
    }

    suspend fun getNotSynchronizedUser(userId: Int): UserVo? {
        val timestamp = getUserTimestamp(userId)
        val year = platformInfo.getCurrentTime().get(Calendar.YEAR)
        return usersDao.getNotSynchronizedUser(
            timestamp = timestamp.thisDeviceTimestamp,
            userId = userId
        ).mapToUsersVo(year).firstOrNull()
    }

    suspend fun updateUserTimestamp(timestamp: UserTimestampEntity) {
        userTimestampDao.insertOrUpdateTimestamp(timestamp)
    }

    private suspend fun createEmptyTimestamp(userId: Int): UserTimestampEntity {
        val timestamp = UserTimestampEntity(
            userId = userId,
            otherDevicesTimestamp = 0,
            thisDeviceTimestamp = 0
        )
        userTimestampDao.insertOrUpdateTimestamp(timestamp)
        return timestamp
    }

    private fun List<UserWithGoals>.mapToUsersVo(year: Int): List<UserVo> {
        return this.map {
            val goalsInCurrentYear = it.goals.find { it.year == year }?.toVo()?.let { goal ->
                UserReadingGoalsInYearsVo(goals = listOf(goal))
            }
            it.user.toVo()
                .copy(userReadingGoalsInYears = goalsInCurrentYear)
        }
    }

}