package platform

enum class Platform {
    DESKTOP,
    MOBILE
}

fun Platform.isDesktop(): Boolean = this == Platform.DESKTOP
fun Platform.isMobile(): Boolean = this == Platform.MOBILE