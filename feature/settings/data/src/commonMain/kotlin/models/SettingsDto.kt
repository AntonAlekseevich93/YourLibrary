package models

import kotlinx.serialization.Serializable
import main_models.settings.SettingsVo


@Serializable
data class SettingsDto(
    val libraryName: String?,
    val currentTheme: String?,
)

fun SettingsVo.toDto() = SettingsDto(
    libraryName = libraryName,
    currentTheme = currentTheme,
)

fun SettingsDto.toVo(): SettingsVo? {
    return SettingsVo(
        libraryName = libraryName ?: return null,
        currentTheme = currentTheme ?: return null,
    )
}