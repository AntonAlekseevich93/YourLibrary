package main_models.rest.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import main_models.user.UserGoalInYearVo
import main_models.user.UserReadingGoalsInYearsVo

@Serializable
data class UserReadingGoalsInYearsDto(
    @SerialName("goals") val goals: List<UserGoalInYearDto>?,
)

@Serializable
data class UserGoalInYearDto(
    @SerialName("year") val year: Int?,
    @SerialName("booksGoal") val booksGoal: Int?,
)

fun UserReadingGoalsInYearsDto.toVo(): UserReadingGoalsInYearsVo? {
    return UserReadingGoalsInYearsVo(
        goals = goals?.mapNotNull { it.toVo() }.takeIf { it?.isEmpty() == false }
            ?: return null
    )
}

fun UserGoalInYearDto.toVo(): UserGoalInYearVo? {
    return UserGoalInYearVo(
        year = year ?: return null,
        booksGoal = booksGoal ?: return null,
    )
}

fun UserReadingGoalsInYearsVo.toDto(): UserReadingGoalsInYearsDto? {
    return UserReadingGoalsInYearsDto(
        goals = goals.map { it.toDto() }.takeIf { it.isNotEmpty() }
            ?: return null
    )
}

fun UserGoalInYearVo.toDto(): UserGoalInYearDto {
    return UserGoalInYearDto(
        year = year,
        booksGoal = booksGoal,
    )
}


