package main_models.user

data class UserVo(
    val id: Long,
    val name: String,
    val email: String,
    val isVerified: Boolean
)