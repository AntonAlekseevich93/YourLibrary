package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.user.UserVo

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @SerialName("email") val email: String,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("isVerified") val isVerified: Boolean,
    @SerialName("isAuthorized") val isAuthorized: Boolean,
)

fun UserEntity.toVo(): UserVo? {
    return UserVo(
        id = id,
        name = name,
        email = email,
        isVerified = isVerified,
        isAuth = isAuthorized
    )
}