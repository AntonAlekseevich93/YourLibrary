package main_models.local_models

import main_models.user.UserVo

data class UserLocalDto(
    val id: Long?,
    val name: String?,
    val email: String?,
    val isVerified: Long?
)

fun UserVo.toDto() = UserLocalDto(
    id = id,
    name = name,
    email = email,
    isVerified = if (isVerified) 0 else 1
)

fun UserLocalDto.toVo(): UserVo? {
    return UserVo(
        id = id ?: return null,
        name = name ?: return null,
        email = email ?: return null,
        isVerified = isVerified == 0L
    )
}