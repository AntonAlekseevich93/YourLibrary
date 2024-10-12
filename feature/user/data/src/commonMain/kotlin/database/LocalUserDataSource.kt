package database

import database.room.RoomMainDataSource
import database.room.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalUserDataSource(
    roomDb: RoomMainDataSource,
) {
    private val usersDao = roomDb.usersDao

    suspend fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Boolean,
        isAuthorized: Boolean,
    ) {
        val user = usersDao.getUserByEmail(email).firstOrNull()
        if (user == null) {
            usersDao.insertUser(
                UserEntity(
                    id = id,
                    name = name,
                    email = email,
                    isVerified = isVerified,
                    isAuthorized = isAuthorized
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

    suspend fun getAuthorizedUser(): Flow<UserEntity?> = flow {
        usersDao.getAuthorizedUser().collect {
            val users = it.filter { it.isAuthorized }
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

}