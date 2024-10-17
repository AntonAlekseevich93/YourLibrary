package database.room.entities.user

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithGoals(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userGoalUserId"
    )
    val goals: List<UserGoalInYearEntity>
)