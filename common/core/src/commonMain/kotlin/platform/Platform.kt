package platform

sealed class Platform(val isDebug: Boolean) {
    class DESKTOP(isDebug: Boolean) : Platform(isDebug)
    class MOBILE : Platform(false)
}

fun Platform.isDesktop(): Boolean = this is Platform.DESKTOP
fun Platform.isMobile(): Boolean = this is Platform.MOBILE