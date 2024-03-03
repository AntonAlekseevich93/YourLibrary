package build_variants

enum class DesktopBuildVariants(val buildVariantsName: String) {
    DEBUG("desktop_debug"),
    RELEASE("desktop_release")
}

fun DesktopBuildVariants.isDebug(): Boolean = this == DesktopBuildVariants.DEBUG