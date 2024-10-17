package main_models.user

data class UserReadingGoalsInYearsVo(
    val goals: List<UserGoalInYearVo>,
)

data class UserGoalInYearVo(
    val year: Int,
    val booksGoal: Int,
)

