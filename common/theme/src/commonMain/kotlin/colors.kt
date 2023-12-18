import androidx.compose.ui.graphics.Color

data class AppColors(
    val mainBackgroundColor: Color,
    val mainBackgroundWindowDarkColor: Color,
    val mainTextColor: Color,
    val mainPlaceholderTextColor: Color,
    val mainIconsColor: Color,
    val cardBackgroundDark: Color,
    val cardBackgroundLight: Color,
    val tooltipAreaBackground: Color,
    val divider: Color,
    val searchBackground: Color,
    val searchIconColor: Color,
    val searchDescriptionTextColor: Color,
    val searchDividerColor: Color,
    val pointerIsActiveCardColor: Color,
    val pointerIsActiveCardColorDark: Color,
    val mainIconsColorDark: Color
)

val lightPaletteTheme = AppColors(
    mainBackgroundColor = Color(0xFF151515),
    mainBackgroundWindowDarkColor = Color(0xFF2A2A2A),
    mainTextColor = Color(0xFFe5e5e5),
    mainPlaceholderTextColor = Color(0xFFb2b2b2),
    mainIconsColor = Color(0xFFd4d4d4),
    mainIconsColorDark = Color(0xFF2a2a2a),
    cardBackgroundDark = Color(0xFF2A2A2A),
    cardBackgroundLight = Color(0xFF3A3A3D),
    tooltipAreaBackground = Color(0xFF1b1c21),
    divider = Color(0xFF212121),
    searchBackground = Color(0xFF262626),
    searchIconColor = Color(0xFF878787),
    searchDescriptionTextColor = Color(0xFFc3c3c3),
    searchDividerColor = Color(0xFF3b3b3b),
    pointerIsActiveCardColor = Color(0xFF4D4D50),
    pointerIsActiveCardColorDark = Color(0xFF3d3d40),
)

val darkPaletteTheme = AppColors(
    mainBackgroundColor = Color(0xFF151515),
    mainBackgroundWindowDarkColor = Color(0xFF2A2A2A),
    mainTextColor = Color(0xFFe5e5e5),
    mainPlaceholderTextColor = Color(0xFFb2b2b2),
    mainIconsColor = Color.White,
    mainIconsColorDark = Color(0xFF4D4D50),
    cardBackgroundDark = Color(0xFF2A2A2A),
    cardBackgroundLight = Color(0xFF3A3A3D),
    tooltipAreaBackground = Color(0xFF1b1c21),
    divider = Color(0xFF252525),
    searchBackground = Color(0xFF262626),
    searchIconColor = Color(0xFF878787),
    searchDescriptionTextColor = Color(0xFF9f9f9f),
    searchDividerColor = Color(0xFF9f9f9f),
    pointerIsActiveCardColor = Color(0xFF4D4D50),
    pointerIsActiveCardColorDark = Color(0xFF3d3d40),
)