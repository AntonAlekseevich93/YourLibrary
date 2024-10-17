package database.room.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.user.UserVo

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @SerialName("email") val email: String,
    @SerialName("userId") val userId: Int,
    @SerialName("name") val name: String,
    @SerialName("isVerified") val isVerified: Boolean,
    @SerialName("isAuthorized") val isAuthorized: Boolean,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long,
)

fun UserEntity.toVo(): UserVo {
    return UserVo(
        id = userId,
        name = name,
        email = email,
        isVerified = isVerified,
        isAuth = isAuthorized,
        timestampOfUpdating = timestampOfUpdating,
        userReadingGoalsInYears = null
    )
}

fun UserVo.toEntity(): UserEntity {
    return UserEntity(
        userId = id,
        name = name,
        email = email,
        isVerified = isVerified,
        isAuthorized = isAuth,
        timestampOfUpdating = timestampOfUpdating,
    )
}