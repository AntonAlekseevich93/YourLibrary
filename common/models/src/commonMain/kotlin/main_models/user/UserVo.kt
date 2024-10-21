package main_models.user

data class UserVo(
    val id: Int,
    val name: String,
    val email: String,
    val isVerified: Boolean,
    val isAuth: Boolean,
    val isModerator: Boolean,
    val timestampOfUpdating: Long,
    val userReadingGoalsInYears: UserReadingGoalsInYearsVo?,
)