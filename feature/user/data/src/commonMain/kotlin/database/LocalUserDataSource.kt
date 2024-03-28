package database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import main_models.local_models.UserLocalDto

class LocalUserDataSource(
    private val db: SqlDelightDataSource
) {
    fun createOrUpdateUser(
        id: Int,
        name: String,
        email: String,
        isVerified: Int,
        isAuthorized: Int,
    ) {
        if (db.appQuery.getUserByEmail(email).executeAsOneOrNull() == null) {
            db.appQuery.createUser(
                id = id.toLong(),
                name = name,
                email = email,
                isVerified = isVerified.toLong(),
                isAuthorized = isAuthorized.toLong()
            )
        } else {
            db.appQuery.updateUserByEmail(
                id = id.toLong(),
                name = name,
                email = email,
                isVerified = isVerified.toLong(),
                isAuthorized = isAuthorized.toLong()
            )
        }
    }

    suspend fun getAuthorizedUser(): Flow<UserLocalDto?> {
        return db.appQuery.getAuthorizedUser().asFlow().mapToList(Dispatchers.IO).map {
            val table = it.firstOrNull()
            UserLocalDto(
                id = table?.id,
                name = table?.name,
                email = table?.email,
                isVerified = table?.isVerified
            )
        }
    }

    fun logOut() {
        db.appQuery.logOut()
    }

}