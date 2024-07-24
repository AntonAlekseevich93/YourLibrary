import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppTheme(val id: Int) {
    DARK(id = 0),
    LIGHT(id = 1)
}

@Composable
fun AppTheme(
    appTheme: AppTheme? = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colors = when (appTheme) {
        AppTheme.LIGHT -> lightPaletteTheme
        AppTheme.DARK -> darkPaletteTheme
        else -> lightPaletteTheme
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides MainTypography(),
        content = content
    )
}

object ApplicationTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

}

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}
val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No font provided")
}