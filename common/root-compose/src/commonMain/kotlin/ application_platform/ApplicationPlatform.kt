package application_platform

import platform.Platform


fun ApplicationPlatform.convert(): Platform = when (this) {
    ApplicationPlatform.DESKTOP -> Platform.DESKTOP
    ApplicationPlatform.MOBILE -> Platform.MOBILE
}

fun ApplicationPlatform.isDesktop(): Boolean = this == ApplicationPlatform.DESKTOP

enum class ApplicationPlatform {
    DESKTOP,
    MOBILE
}
