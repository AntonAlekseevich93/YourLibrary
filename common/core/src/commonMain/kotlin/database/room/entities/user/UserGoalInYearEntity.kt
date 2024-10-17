package database.room.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.user.UserGoalInYearVo

@Entity
data class UserGoalInYearEntity(
    @PrimaryKey(autoGenerate = false)
    @SerialName("year") val year: Int,
    @SerialName("userGoalUserId") val userGoalUserId: Int,
    @SerialName("goal") val booksGoal: Int,
)

fun UserGoalInYearEntity.toVo(): UserGoalInYearVo {
    return UserGoalInYearVo(
        year = year,
        booksGoal = booksGoal
    )
}

fun UserGoalInYearVo.toEntity(
    userId: Int,
): UserGoalInYearEntity {
    return UserGoalInYearEntity(
        year = year,
        booksGoal = booksGoal,
        userGoalUserId = userId
    )
}